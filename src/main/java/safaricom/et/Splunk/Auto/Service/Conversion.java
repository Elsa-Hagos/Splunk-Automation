package safaricom.et.Splunk.Auto.Service;



import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;


@Service
public class Conversion {
    public static File toExcel(byte[] bytes, String fileName) throws IOException {
         Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("SplunkData");
            String[] lines = new String(bytes).split("\n");
            int rowNum = 0;
            for (String line : lines) {
                String[] values = line.split(",");
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (String value : values) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(value);}
            }
            File excelFile = new File(fileName);
            try (FileOutputStream outputStream = new FileOutputStream(excelFile)) {
                workbook.write(outputStream);
            } catch (IOException e) {
                throw new IOException("Failed to write Excel file", e);
            }
            return excelFile;

        }
    public static byte[] toByteArray(File excelFile) throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(excelFile);
            bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buf)) != -1) {
                bos.write(buf, 0, bytesRead);
            }
            return bos.toByteArray();
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }

}

