package com.king.mysqlitedatabaseapp

import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Create your database
        var db:SQLiteDatabase = openOrCreateDatabase("registrations", Context.MODE_PRIVATE,null)
        //Create a table inside the database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(jina VARCHAR, arafa VARCHAR, kitambulisho VARCHAR)")

        //Set a listener on your button save to implement the saving
        mBtnSave.setOnClickListener {
            //Receive data from the user
            var name = mEdtName.text.toString()
            var email = mEdtMail.text.toString()
            var id_number = mEdtIdNumber.text.toString()
            //Check if the user is trying to submit empty records
            if (name.isEmpty() or email.isEmpty() or id_number.isEmpty()){
                //Use the display_message() to Display a message telling the user to fill all the inputs
                display_message("EMPTY FIELDS","Please fill all the inputs")
            }else{
                //Proceed to save your data into the db
                db.execSQL("INSERT INTO users VALUES('"+name+"','"+email+"','"+id_number+"')")
                display_message("SUCCESS","User saved successfully")
                clear()
            }
        }

        //Set a listener on button view to see the saved records
        mBtnView.setOnClickListener {
            //Use a cursor to select data from the db
            var cursor = db.rawQuery("SELECT * FROM users",null)
            //Check if there is any record in the db
            if (cursor.count == 0){
                //Use the display_message() to tell the user there are no records in the db
                display_message("NO RECORD","Sorry!!! No records were found")
            }else{
                //Use the buffer to append the records
                var buffer = StringBuffer()
                while (cursor.moveToNext()){
                    buffer.append(cursor.getString(0)+"\n")
                    buffer.append(cursor.getString(1)+"\n")
                    buffer.append(cursor.getString(2)+"\n\n")
                }
                //Use the display_message() to show the records to the user
                display_message("USERS",buffer.toString())
            }
        }

        //Set a listener to our button delete to implement the deletion
        mBtnDelete.setOnClickListener {
            //Get the ID from the user
            var id_number = mEdtIdNumber.text.toString()
            //Check if the user is trying to click delete button with an empty ID field
            if(id_number.isEmpty()){
                display_message("EMPTY FIELD","Please fill the ID input field")
            }else{
                //Use the cursor to select the user with the given ID
                var cursor = db.rawQuery("SELECT * FROM users WHERE kitambulisho='"+id_number+"'",null)
                //Check if the record is available in the database
                if (cursor.count==0){
                    display_message("NO RECORD","Sorry!!! There is no user with that ID in the database")
                }else{
                    //Finally delete the record
                    db.execSQL("DELETE FROM users WHERE kitambulisho='"+id_number+"'")
                    display_message("SUCCESS","The user was deleted successfully")
                    clear()
                }
            }
        }

    }

    //This is a function to display messages to the user
    private fun display_message(title:String, message:String){
        var alerDialog:AlertDialog.Builder = AlertDialog.Builder(this)
        alerDialog.setCancelable(false)
        alerDialog.setTitle(title)
        alerDialog.setMessage(message)
        alerDialog.setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        alerDialog.create().show()
    }

    //This is a function to clear the input fields once the intended request is successful
    private fun clear(){
        mEdtName.setText(null)
        mEdtMail.setText(null)
        mEdtIdNumber.setText(null)
    }
}
