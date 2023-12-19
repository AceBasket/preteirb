package com.example.preteirb.data.cache.current_user

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.example.preteirb.CurrentUserInfo
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class CurrentUserSerializer @Inject constructor() : Serializer<CurrentUserInfo> {
    override val defaultValue: CurrentUserInfo = CurrentUserInfo.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CurrentUserInfo =
        try {
            // readFrom is already called on the data store background thread
            CurrentUserInfo.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: CurrentUserInfo, output: OutputStream) {
        // writeTo is already called on the data store background thread
        t.writeTo(output)
    }
}