package com.elevendirtymind.clubmembermanager.excel;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.elevendirtymind.clubmembermanager.application.MemberApplication;
import com.elevendirtymind.clubmembermanager.model.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Export {
    private static final String filename = "members.xlsx";
    // Initialize Firebase Storage reference
    private static StorageReference storageRef;
    String fileContents = "Your file content goes here";

    // Method to check if external storage is writable
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static void exportTest(@NonNull Context context, @NonNull MemberApplication memberApplication, @NonNull List<Member> members) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Members");

            // Get the current date and time
            String pattern = "dd/MM/yyyy HH:mm:ss"; // Define the date and time format
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.getDefault());
            String dateTime = dateFormat.format(new Date());

            // Create a row above the header
            Row timeRow = sheet.createRow(0);
            timeRow.createCell(0).setCellValue("Thời Gian: " );
            timeRow.createCell(1).setCellValue(dateTime);

            // Create header row
            Row headerRow = sheet.createRow(1);
            String[] headers = {"Mã Sinh Viên", "Họ và Tên", "Số Điện Thoại", "Quê Quán", "Chức Vụ", "Chuyên Ngành", "Khoa", "Lớp"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Populate data rows
            for (int i = 0; i < members.size(); i++) {
                Member member = members.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(member.getMaSinhVien());
                row.createCell(1).setCellValue(member.getHoTen());
                row.createCell(2).setCellValue(member.getSdt());
                row.createCell(3).setCellValue(member.getQueQuan());
                row.createCell(4).setCellValue(member.getRole());
                row.createCell(5).setCellValue(member.getChuyenNganh());
                row.createCell(6).setCellValue(member.getKhoa());
                row.createCell(7).setCellValue(member.getLop());
            }

            // Get the directory for the app's internal files
            File internalDir = context.getFilesDir();
            Log.i("BECHJ", "excel.Export :: exportTest() :: internalDir: " + internalDir.getAbsolutePath());

            // Specify the file path in internal storage
            File file = new File(internalDir, "members.xlsx");
            /**
             * /data/user/0/com.elevendirtymind.clubmembermanager/files
             */
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                Log.i("BECHJ", "excel.Export :: exportTest() :: File written to internal storage: " + file.getAbsolutePath());


                /**
                 * Uploads to firebase Storage
                 */
                Uri fileUri = Uri.fromFile(file);
                Log.i("BECHJ", "excel.Export :: exportTest() ::  filePath() : " + fileUri.getPath() );
                String fileName = "members.xlsx";
                storageRef = memberApplication.getFireStorage().getReference();
                StorageReference fileRef = storageRef.child(fileName);

                InputStream stream = Files.newInputStream(Paths.get(fileUri.getPath()));

                UploadTask uploadTask = fileRef.putStream(stream);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        exception.printStackTrace();
                        Log.i("BECHJ", "excel.Export :: exportTest() :: Firebase :: ERROR: " +exception.getMessage() );
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        Log.i("BECHJ", "excel.Export :: exportTest() :: Firease: DONE");
                        Toast.makeText(context, "Bạn có thể tìm thấy `members.xlsx` trong thư mục Download của thiết bị", Toast.LENGTH_LONG).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            // The upload is successful
                            Log.d("BECHJ", "onCompleteListenter() OVERRIDE :: Upload successful");

                        } else {
                            /**
                             * Override the existing file on firebase
                             */
                            // The upload failed, handle error here
                            Exception e = task.getException();
                            if (e.getMessage() != null && e.getMessage().contains("object")) {
                                // If the file already exists, overwrite it
                                fileRef.putStream(stream);
                                Log.d("BECHJ", "onCompleteListenter() OVERRIDE :: Upload done");
                                Toast.makeText(context, "Bạn có thể tìm thấy `members.xlsx` trong thư mục Download của thiết bị", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                /**
                 * Then download
                 */
                // Get a reference to the Firebase Storage instance
                FirebaseStorage storage = FirebaseStorage.getInstance();

                // Create a reference to the file you want to download
                StorageReference fileRefDowload = storage.getReference().child("members.xlsx");

                // Download the file to a local file on the device
                // Specify the download directory
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File localFile = new File(downloadsDir, "members.xlsx");

                fileRef.getFile(localFile)
                        .addOnSuccessListener(taskSnapshot -> {
                            // File download success
                            Log.d("BECHJ", "excel.Export :: exportTest() :: Firease: File downloaded to " + localFile.getAbsolutePath());
                            // Handle the downloaded file, such as displaying it to the user
                        })
                        .addOnFailureListener(exception -> {
                            // File download failed
                            Log.e("BECHJ", "excel.Export :: exportTest() :: Firease: Error downloading file: " + exception.getMessage());
                            // Handle the error, such as displaying an error message to the user
                        });


            } catch (Exception e) {
                e.printStackTrace();
                Log.i("BECHJ", "excel.Export :: exportTest() :: ERROR: " + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("BECHJ", "excel.Export :: exportTest() :: ERROR: " + e.getMessage());
        }
    }

    public static void exportMembersToExcel(List<Member> members) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Members");

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Mã Sinh Viên", "Họ và Tên", "Số Điện Thoại", "Quê Quán", "Chức Vụ", "Chuyên Ngành", "Khoa", "Lớp"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Populate data rows
            for (int i = 0; i < members.size(); i++) {
                Member member = members.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(member.getMaSinhVien());
                row.createCell(1).setCellValue(member.getHoTen());
                row.createCell(2).setCellValue(member.getSdt());
                row.createCell(3).setCellValue(member.getQueQuan());
                row.createCell(4).setCellValue(member.getRole());
                row.createCell(5).setCellValue(member.getChuyenNganh());
                row.createCell(6).setCellValue(member.getKhoa());
                row.createCell(7).setCellValue(member.getLop());
            }

            // Check if external storage is available
            if (isExternalStorageWritable()) {
                // Get the Downloads directory
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                // Write to file
                try (FileOutputStream fileOut = new FileOutputStream("members.xlsx")) {
                    workbook.write(fileOut);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("BECHJ", "excel.Export :: exportMemberToExcel() :: isExternalStorageWritable() :: ERROR: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("BECHJ", "excel.Export :: exportMemberToExcel() :: ERROR: " + e.getMessage());
        }
    }
}