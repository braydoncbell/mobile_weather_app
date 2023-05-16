package com.braydon.android.forecast.presentation

import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.braydon.android.forecast.R
import com.braydon.android.forecast.presentation.ui.theme.DeepBlue
import java.util.*

@Composable
fun CalendarSelectionButton(onDateSelected: (Int, Int) -> Unit) {
    val context = LocalContext.current

    Button(
        onClick = {
            showDatePickerDialog(context, onDateSelected)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = DeepBlue,
            contentColor = Color.White
        ),
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .height(46.dp),
    ) {
        Text(text = "Select Date")
    }
}

private fun showDatePickerDialog(context: Context, onDateSelected: (Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()

    val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            onDateSelected(month, dayOfMonth)
        }

    DatePickerDialog(
        context,
        dateSetListener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}


@Composable
fun DatePicker(
    selectedDate: Calendar,
    onDateSelected: (Calendar) -> Unit
) {
    val datePickerDialogShown = remember { mutableStateOf(false) }
    val selectedDateState = remember { mutableStateOf(selectedDate) }

    Column {
        Button(
            onClick = { datePickerDialogShown.value = true },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Select Date")
        }

        if (datePickerDialogShown.value) {
            AlertDialog(
                onDismissRequest = { datePickerDialogShown.value = false },
                title = { Text(text = "Select Date") },
                confirmButton = {
                    Button(
                        onClick = {
                            onDateSelected(selectedDateState.value)
                            datePickerDialogShown.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = DeepBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(top = 76.dp)
                            .fillMaxWidth()
                            .height(46.dp),
                        enabled = false
                    ) {
                        Text(text = "Confirm")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { datePickerDialogShown.value = false }
                    ) {
                        Text(text = "Cancel")
                    }
                },
                text = {
                    // Display the DatePicker component or any other custom date picker UI
                    // and update the selectedDateState accordingly
                    // ...
                    // Example: Display selected date as text
                    Text(text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDateState.value.time))
                }
            )
        }
    }
}