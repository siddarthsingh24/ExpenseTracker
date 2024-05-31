@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R
import com.example.expensetracker.android.Utils
import com.example.expensetracker.data.model.ExpenseEntity
import com.example.expensetracker.viewmodel.AddExpenseViewModel
import com.example.expensetracker.viewmodel.AddExpenseViewModelFactory
import com.example.expensetracker.widget.ExpenseTextView
import kotlinx.coroutines.launch
import androidx.compose.material3.TextButton as TextButton1

@Composable
fun AddExpense(navController: NavController) {
    val viewModel=AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
    val coroutineScope= rememberCoroutineScope()
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (nameRow, list, card, topBar) = createRefs()
                Image(painter = painterResource(id = R.drawable.ic_topbar),
                    contentDescription = null,
                    modifier = Modifier.constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
                Box(modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }){
                    Image(
                        painter = painterResource(id = R.drawable.chevron_left), contentDescription = null,
                        modifier=Modifier.align(Alignment.CenterStart)
                    )
                   ExpenseTextView(text = "Add Expense",
                       fontSize = 28.sp,
                       fontWeight = FontWeight.Bold,
                       color = Color.White,
                       modifier = Modifier
                           .padding(16.dp)
                           .align(Alignment.Center))
                    Image(
                        painter = painterResource(id = R.drawable.dots_menu), contentDescription = null,
                        modifier=Modifier.align(Alignment.CenterEnd)
                    )
                }
                DataForm(modifier= Modifier
                    .padding(top = 60.dp)
                    .constrainAs(card) {
                        top.linkTo(nameRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }, onAddExpenseClick ={
                        coroutineScope.launch{
                           if( viewModel.addExpense(it)){
                               navController.popBackStack()
                           }

                        }

                } )


            }


        }
    }
@Composable
fun DataForm(modifier: Modifier, onAddExpenseClick:(model:ExpenseEntity)->Unit){
    val name= remember{
        mutableStateOf("")
    }
    val amount= remember{
        mutableStateOf("")
    }
    val date=remember{
        mutableStateOf(0L)
    }
    val dateDialogVisibility=remember{
        mutableStateOf(false)
    }
    val category= remember {
        mutableStateOf("")

    }
    val type= remember{
        mutableStateOf("")
    }
    Column(modifier= modifier
        .padding(10.dp)
        .fillMaxWidth()
        .shadow(8.dp)
        .background(Color.White)
        .clip(RoundedCornerShape(16.dp))
        .padding(10.dp)
        .height(520.dp)
        .verticalScroll(rememberScrollState())){

        ExpenseTextView(text = "Name", fontSize=14.sp)
        Spacer(modifier=Modifier.size(4.dp))
        OutlinedTextField(value = name.value, onValueChange ={
            name.value=it
        }, modifier = Modifier.fillMaxWidth() )
        ExpenseTextView(text = "Amount", fontSize=14.sp)
        Spacer(modifier=Modifier.size(4.dp))
        OutlinedTextField(value = amount.value, onValueChange ={
            amount.value=it
        }, modifier=Modifier.fillMaxWidth() )
        ExpenseTextView(text = "Date", fontSize=14.sp)
        Spacer(modifier=Modifier.size(8.dp))
        OutlinedTextField(value = if(date.value == 0L)"" else Utils.formatDatetToHumanReadableFormat(date.value) , onValueChange = {
        }, modifier= Modifier
            .fillMaxWidth()
            .clickable { dateDialogVisibility.value = true }, enabled = false)
        ExpenseDatePickerDialog(onDateSelected = {}, onDismiss ={} )
        ExpenseTextView(text = "Date", fontSize=14.sp)
        Spacer(modifier=Modifier.size(4.dp))
        ExpenseDropDown(listofItems = listOf("Income","Expense"), onItemSelected = {
            type.value=it
        })
        Spacer(modifier=Modifier.size(8.dp))
            

        Button(onClick = {
                         val model= ExpenseEntity(null,name.value,amount.value.toDoubleOrNull()?: 0.0,Utils.formatDatetToHumanReadableFormat(date.value),category.value, type.value
                         )
            onAddExpenseClick(model)

        },
            modifier= Modifier
                .clip(RoundedCornerShape(2.dp))
                .fillMaxWidth()) {
            ExpenseTextView(text = "Add Expense",
                fontSize = 14.sp,
                color = Color.White)
        }
    }
    if (dateDialogVisibility.value) {
        ExpenseDatePickerDialog(onDateSelected = {
            date.value = it
            dateDialogVisibility.value = false
        }, onDismiss = {
            dateDialogVisibility.value = false
        })


    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    onDateSelected: (date: Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    val selectedDate = datePickerState.selectedDateMillis ?: 0L // Provide a default value when null

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton1(onClick = { onDateSelected(selectedDate) }) {
                ExpenseTextView(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton1(onClick = { onDateSelected(selectedDate) }) {
                ExpenseTextView(text = "Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun ExpenseDropDown(listofItems:List<String>, onItemSelected:(items:String)->Unit){
    val expanded= remember {
        mutableStateOf( false)
    }
    val selectedItem=remember{
        mutableStateOf(listofItems[0])
    }
    ExposedDropdownMenuBox(expanded=expanded.value, onExpandedChange={expanded.value=it}){
        TextField(value=selectedItem.value, onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = (expanded.value))
            })
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = {  }) {
            listofItems.forEach{
                DropdownMenuItem(text = { Text(text=it)}, onClick = { selectedItem.value=it
                    onItemSelected(selectedItem.value)
                    expanded.value=false})
            }
            
        }

    }
}






@Preview(showBackground = true)
@Composable
fun AddExpensePreview(){
    AddExpense(rememberNavController())
}


