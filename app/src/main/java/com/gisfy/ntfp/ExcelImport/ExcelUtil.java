package com.gisfy.ntfp.ExcelImport;




import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.gisfy.ntfp.R;
import com.gisfy.ntfp.Utils.SnackBarUtils;
import com.gisfy.ntfp.VSS.Collectors.ImportExcel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelUtil {
    private static final String TAG = "UTIL TAG";
    public static List<Map<Integer, Object>> readExcelNew(Context context, Uri uri, String filePath) {
        List<Map<Integer, Object>> list = null;
        Workbook wb;
        if (filePath == null) {
            return null;
        }
        String extString;
        if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
            Log.e(TAG, "Please select the correct Excel file");
            return null;
        }
        extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is;
        try {
            is = context.getContentResolver().openInputStream(uri);
            Log.i(TAG, "readExcel: " + extString);
            if (".xls".equals(extString)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(extString)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
            if (wb != null) {
                // used to store data
                list = new ArrayList<>();
                // get the first sheet
                Sheet sheet = wb.getSheetAt(0);
                // get the first line header
                Row rowHeader = sheet.getRow(0);
                int cellsCount = rowHeader.getPhysicalNumberOfCells();
                //store header to the map
                Map<Integer, Object> headerMap = new HashMap<>();
                for (int c = 0; c < cellsCount; c++) {
                    Object value = getCellFormatValue(rowHeader.getCell(c));
                    String cellInfo = "header " + "; c:" + c + "; v:" + value;
                    Log.i(TAG, "readExcelNew: " + cellInfo);
                    headerMap.put(c, value);
                }
                //add  headermap to list
//                list.add(headerMap);

                // get the maximum number of rows
                int rownum = sheet.getPhysicalNumberOfRows();
                // get the maximum number of columns
                int colnum = headerMap.size();
                Log.i("conum",rownum+"");
                //index starts from 1,exclude header.
                if (colnum==18) {
                    for (int i = 1; i < rownum; i++) {
                        Row row = sheet.getRow(i);
                        //storing subcontent
                        Map<Integer, Object> itemMap = new HashMap<>();
                        if (row != null && rownum < 200) {
                            for (int j = 0; j < colnum; j++) {
                                Object value = getCellFormatValue(row.getCell(j));
                                String cellInfo = "r: " + i + "; c:" + j + "; v:" + value;
                                Log.i(TAG, "readExcelNew: " + cellInfo);
                                if (value != "")
                                    itemMap.put(j, value);
                                else
                                    itemMap.put(null, null);
                            }
                        } else {
                            break;
                        }
                        list.add(itemMap);
                    }
                }
                                    Toast.makeText(context, "Not a valid Excel", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "readExcelNew: import error " + e);
            Activity activity=(Activity)context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SnackBarUtils.ErrorSnack((Activity)context,context.getString(R.string.importerror));
                }
            });
        }
        return list;
    }


    private static Object getCellFormatValue(Cell cell) {
        Object cellValue;
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    cellValue = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC: {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA: {
                    // determine if the cell is in date format
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // Convert to date format YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    } else {
                        // Numeric
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING: {
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        } else {
            cellValue = "";
        }
        return cellValue;
    }
}
