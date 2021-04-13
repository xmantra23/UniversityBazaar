package org.samir.universitybazaar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.samir.universitybazaar.Models.Loan;
import org.samir.universitybazaar.Models.Sell;

import java.util.ArrayList;

public class LoanDAO {

    private DatabaseHelper dbHelper;

    public LoanDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public boolean addLoan(Loan loan){
        if(dbHelper != null){
            SQLiteDatabase db = null;
            try {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("title", loan.getTitle());
                values.put("description", loan.getDescription());
                values.put("creatorName", loan.getCreatorName());
                values.put("creatorId", loan.getCreatorId());
                values.put("createdDate", loan.getCreatedDate());
                values.put("image", loan.getImage());
                values.put("price", loan.getPrice());
                values.put("status", "for loan");
                values.put("day", "default");

                long rowId = db.insert("loan",null,values);
                db.close();

                if(rowId != -1){ //insert was successful
                    return true;
                }else{ //insert failed.
                    return false;
                }
            } catch (SQLException e) {
                //error inserting in database
                e.printStackTrace();
                db.close();
                return false;
            }finally {
                db.close();
            }
        }else{
            //couldn't connect to the database
            return false;
        }
    }

    public ArrayList<Loan> getLoanByMemberId(String memberId){
        ArrayList<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "image",
                    "price",
                    "status",
                    "day"
            };

            String[] args = new String[]{
                    memberId
            };

            //retrieve all the loans from the loans table where creatorId == memberId
            Cursor cursor = db.query("loan", columns, "creatorId=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ // continues until all the retrieved rows have been iterated.
                        Loan loan = new Loan();
                        int loanId = cursor.getInt(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String description = cursor.getString(cursor.getColumnIndex("description"));
                        String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                        String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                        String image = cursor.getString(cursor.getColumnIndex("image"));
                        String price = cursor.getString(cursor.getColumnIndex("price"));
                        String status = cursor.getString(cursor.getColumnIndex("status"));


                        loan.set_id(loanId);
                        loan.setTitle(title);
                        loan.setDescription(description);
                        loan.setCreatorId(creatorId);
                        loan.setCreatorName(creatorName);
                        loan.setCreatedDate(createdDate);
                        loan.setImage(image);
                        loan.setPrice(price);
                        loan.setStatus(status);

                        loans.add(loan); //add the loan to the loans arraylist.

                        if(cursor.isLast()){ // we are at the last row of the dataset. no need to continue anymore.
                            isLast = true;
                        }else{
                            cursor.moveToNext(); //move to the next row in the dataset.
                        }
                    }
                }
                db.close();
                cursor.close();
                return loans; //return all the loans we got for that memberId in the loans table.
            }else{
                db.close();
                cursor.close();
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            db.close();
            return null;
        }
    }

    public Loan getLoanById(int loanIdIn){
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "image",
                    "price",
                    "status",
                    "day"
            };

            String[] args = new String[]{
                    loanIdIn + "" //doing this to convert int to string. selection arguments must be strings in the sql query.
            };

            //retrieve the loan from the loans table whose _id == loanID
            Cursor cursor = db.query("loan", columns, "_id=?", args, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    Loan loan = new Loan();
                    int loanId = cursor.getInt(cursor.getColumnIndex("_id"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                    String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                    String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                    String image = cursor.getString(cursor.getColumnIndex("image"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    String status = cursor.getString(cursor.getColumnIndex("status"));


                    loan.set_id(loanId);
                    loan.setTitle(title);
                    loan.setDescription(description);
                    loan.setCreatorId(creatorId);
                    loan.setCreatorName(creatorName);
                    loan.setCreatedDate(createdDate);
                    loan.setImage(image);
                    loan.setPrice(price);
                    loan.setStatus(status);


                    return loan;
                }
                db.close();
                cursor.close();
                return null;
            }else{
                db.close();
                cursor.close();
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            db.close();
            return null;
        }
    }

    public ArrayList<Loan> getAllLoans(){
        ArrayList<Loan> loans = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = new String[]{
                    "_id",
                    "title",
                    "description",
                    "creatorId",
                    "creatorName",
                    "createdDate",
                    "image",
                    "price",
                    "status",
                    "day"
            };

            //retrieve all the sale in the sells table.
            Cursor cursor = db.query("loan", columns, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    boolean isLast = false;
                    while(!isLast){ // continues until all the retrieved rows have been iterated.
                        Loan loan = new Loan();
                        int loanId = cursor.getInt(cursor.getColumnIndex("_id"));
                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String description = cursor.getString(cursor.getColumnIndex("description"));
                        String creatorId = cursor.getString(cursor.getColumnIndex("creatorId"));
                        String creatorName = cursor.getString(cursor.getColumnIndex("creatorName"));
                        String createdDate = cursor.getString(cursor.getColumnIndex("createdDate"));
                        String image = cursor.getString(cursor.getColumnIndex("image"));
                        String price = cursor.getString(cursor.getColumnIndex("price"));
                        String status = cursor.getString(cursor.getColumnIndex("status"));


                        loan.set_id(loanId);
                        loan.setTitle(title);
                        loan.setDescription(description);
                        loan.setCreatorId(creatorId);
                        loan.setCreatorName(creatorName);
                        loan.setCreatedDate(createdDate);
                        loan.setImage(image);
                        loan.setPrice(price);
                        loan.setStatus(status);

                        loans.add(loan); //add the loan to the loans arraylist.

                        if(cursor.isLast()){ // we are at the last row of the dataset. no need to continue anymore.
                            isLast = true;
                        }else{
                            cursor.moveToNext(); //move to the next row in the dataset.
                        }
                    }
                }
                db.close();
                cursor.close();
                return loans;
            }else{
                db.close();
                cursor.close();
                return null;
            }
        }catch(SQLException e){
            e.printStackTrace();
            db.close();
            return null;
        }
    }

    public Boolean updateLoanStatus(Loan loan) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("status", loan.getStatus());
            db.update("loan", values, "_id = '"+loan.get_id()+"'", null );
            db.close();
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
