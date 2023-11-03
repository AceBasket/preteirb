package com.example.preteirb.ui.screens.newitemusage

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.example.compose.AppTheme
import com.example.preteirb.R
import com.example.preteirb.data.item.Item

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ObjectSelection(
    objectList: List<Item>,
    //itemSelected: ItemDetails,
    onValueChange: (String) -> Unit,
    onAddItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    // Up Icon when expanded and down icon when collapsed
    val dropdownIcon = if (isExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    
    Column(
        modifier = modifier
    ) {
        // Create an Outlined Text Field
        // with icon and not expanded
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = {
                selectedText = it
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to
                    // the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text("Label") },
            trailingIcon = {
                Icon(dropdownIcon, "contentDescription",
                    Modifier.clickable { isExpanded = !isExpanded })
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded
                )
            }
        )
        
        // Create a drop-down menu with list of objects,
        // when clicked, set the Text Field text as the object selected
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            objectList.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.name) },
                    onClick = {
                        selectedText = it.name
                        isExpanded = false
                    },
                )
            }
            
            // last item is a button to add an object
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.add_object)) },
                onClick = onAddItem
            )
        }
    }
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownObjectSelection(
    objectList: List<Item>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Select an object") }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            Log.d(
                "ExposedDropdownObjectSelection",
                "ExposedDropdownMenu: ${objectList.size} items : $objectList"
            )
            objectList.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.name) },
                    onClick = {
                        selectedOptionText = item.name
                        isExpanded = false
                        onValueChange(item.name)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExposedDropdownObjectSelectionPreview() {
    AppTheme {
        val fakeObjectList = listOf(
            Item(1, "Item 1", "Description 1", 1),
            Item(2, "Item 2", "Description 2", 1),
            Item(3, "Item 3", "Description 3", 1),
            Item(4, "Item 4", "Description 4", 1),
            Item(5, "Item 5", "Description 5", 1),
            Item(6, "Item 6", "Description 6", 1),
        )
        ExposedDropdownObjectSelection(
            objectList = fakeObjectList,
            onValueChange = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ObjectSelectionPreview() {
    AppTheme {
        val fakeObjectList = listOf(
            Item(1, "Item 1", "Description 1", 1),
            Item(2, "Item 2", "Description 2", 1),
            Item(3, "Item 3", "Description 3", 1),
            Item(4, "Item 4", "Description 4", 1),
            Item(5, "Item 5", "Description 5", 1),
            Item(6, "Item 6", "Description 6", 1),
        )
        ObjectSelection(
            objectList = fakeObjectList,
            onValueChange = {},
            onAddItem = {},
        )
    }
}