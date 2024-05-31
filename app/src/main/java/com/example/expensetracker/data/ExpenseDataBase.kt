package com.example.expensetracker.data

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDataBase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao


    companion object{
        const val DATABASE_NAME="expense_database"
        @JvmStatic
        fun getDatabase(context: Context): ExpenseDataBase{
            return Room.databaseBuilder(
                    context,
                    ExpenseDataBase::class.java,
                DATABASE_NAME
            ).addCallback(object: Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    InitBasicData(context)

                }
                fun InitBasicData(context: Context){
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao=getDatabase(context).expenseDao()
                        dao.insertExpense(ExpenseEntity(1,"Salary", 500.0,"2024-09-01","Salary","Income"))
                        dao.insertExpense(ExpenseEntity(2,"Paypal", 600.0,"2024-07-01","Paypal","Income"))
                        dao.insertExpense(ExpenseEntity(3,"Netflix", 40.0,"2024-03-01","Netflix","Expense"))
                        dao.insertExpense(ExpenseEntity(4,"Starbucks", 30.0,"2024-04-01","Starbucks","Expense"))


                    }
                }
            }).build()

        }
    }
}