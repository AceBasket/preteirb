package com.example.preteirb.data.user

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.preteirb.api.ProfileApiService
import com.example.preteirb.data.usage.toUsageWithItemAndUser
import com.example.preteirb.model.ProfileDetails
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class NetworkUsersRepository @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val profileApiService: ProfileApiService
) :
    UsersRepository {
    override suspend fun getAllUsersStream() = flow { emit(profileApiService.getProfiles()) }

    override suspend fun getUserStream(id: Int) = flow { emit(profileApiService.getProfile(id)) }

    override suspend fun getAllItemsOwnedByUserStream(id: Int) =
        flow { emit(profileApiService.getItemsOwnedByUser(id)) }

    override suspend fun getAllItemsBookedAndNotOwnedByUserStream(id: Int) =
        flow {
            emit(
                profileApiService.getUsagesAndItemWithOwnerByUser(id)
                    .map { it.toUsageWithItemAndUser() })
        }

    override suspend fun insertUser(user: ProfileDetails): UserDto {
        return updateOrCreateUser(user, true)
    }

    override suspend fun deleteUser(userDto: UserDto) = profileApiService.deleteProfile(userDto.id)

    override suspend fun updateUser(user: ProfileDetails): UserDto {
        return updateOrCreateUser(user, false)
    }

    override suspend fun updateUsername(user: ProfileDetails): UserDto {
        return profileApiService.updateUsername(
            user.id,
            user.username.toRequestBody("text/plain".toMediaTypeOrNull())
        )
    }

    private suspend fun updateOrCreateUser(user: ProfileDetails, isCreate: Boolean): UserDto {
        if (user.profilePicture == Uri.EMPTY) {
            val updatedUserDto =
                UserDto(id = user.id, username = user.username, profilePicture = null)
            Log.d("saveProfile", "updatedUser: $updatedUserDto")
            return if (isCreate) {
                profileApiService.createProfileWithoutPicture(updatedUserDto)
            } else {
                profileApiService.updateProfileWithoutPicture(updatedUserDto.id, updatedUserDto)
            }
        }
        val profilePicturePart = uriToMultipartBody(user.profilePicture)

        // Create RequestBody for username
        val usernameRequestBody = user.username.toRequestBody("text/plain".toMediaTypeOrNull())

        // Call the API to upload the image and create the profile
        return if (isCreate) {
            profileApiService.createProfile(profilePicturePart, usernameRequestBody)
        } else {
            profileApiService.updateProfile(user.id, profilePicturePart, usernameRequestBody)
        }
    }

    private fun uriToMultipartBody(uri: Uri): MultipartBody.Part {
        // Use ContentResolver to obtain the real file path from the Uri
        val contentResolver = applicationContext.contentResolver
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        val filePath: String? = if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = cursor.getString(columnIndex)
            cursor.close()
            path
        } else {
            uri.path
        }

        // Create RequestBody from file
        val file = File(filePath!!)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        // Create MultipartBody.Part from RequestBody
        return MultipartBody.Part.createFormData("profile_pic", file.name, requestFile)
    }

}