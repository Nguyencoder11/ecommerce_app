package ecommerce_app;
import java.time.Duration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestAutomation {
	protected static WebDriver driver;
    private static DecimalFormat df2 = new DecimalFormat("0.00");
    private static ArrayList<TestCase> listTest = new ArrayList<>();
    
    public static void main(String[] args) throws URISyntaxException {
        try {
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\admin\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
            driver = new ChromeDriver();
            driver.get("http://localhost:8080/ecommerce_app_id/login.jsp");

            // Read test cases from Excel
            ClassLoader classLoader = TestAutomation.class.getClassLoader();
            URL resource = classLoader.getResource("datatest.xlsx");

            if (resource == null) {
                throw new FileNotFoundException("Resource not found: datatest.xlsx");
            }

            File file = new File(resource.toURI());
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

//            int sl = sheet.getLastRowNum();
//            for (int i = 1; i <= sl; i++) {
//                XSSFRow row = (XSSFRow) rowIterator.next();
//                processRow(row, i);
//            }
            // Skip the header row and process the rest
            if (rowIterator.hasNext()) {
                rowIterator.next(); // Skip header
            }

            int index = 1;
            while (rowIterator.hasNext()) {
                XSSFRow row = (XSSFRow) rowIterator.next();
                processRow(row, index++);
            }

            // Calculate pass percentage
            double testpass = calculatePassPercentage();

            // Write test results back to Excel
            writeResultsToExcel(sheet, fileInputStream, file);

            System.out.println("Pass: " + df2.format(testpass / listTest.size() * 100.0) + "%");
            System.out.println("Fail: " + df2.format((listTest.size() - testpass) / listTest.size() * 100.0) + "%");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private static void processRow(XSSFRow row, int index) {
        String email = getCellValue(row.getCell(0));
        String password = getCellValue(row.getCell(1));
        String expectedMessage = getCellValue(row.getCell(2));

        Login lg = new Login(email, password, expectedMessage);
        DangNhap(lg, listTest, index);
        System.out.println("Test case " + index + " da chay xong");
        System.out.println("----------------------");
    }

    private static String getCellValue(XSSFCell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case NUMERIC:
                return new DecimalFormat("0.######").format(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue().trim();
            default:
                return "";
        }
    }

    private static double calculatePassPercentage() {
        double testpass = 0;
        for (TestCase tc : listTest) {
            if (tc.getValue().equals("pass")) {
                testpass++;
            }
            System.out.printf("%-30s%-30s", tc.getName(), tc.getValue());
            System.out.println("");
        }
        return testpass;
    }

    private static void writeResultsToExcel(XSSFSheet sheet, FileInputStream fileInputStream, File file) throws IOException {
        for (int i = 0; i < listTest.size(); i++) {
            XSSFRow row = sheet.getRow(i + 1);
            if (row == null) row = sheet.createRow(i + 1);
            XSSFCell cell = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(listTest.get(i).getValue());
        }
        fileInputStream.close();

        try (FileOutputStream out = new FileOutputStream(file)) {
            XSSFWorkbook workbook = sheet.getWorkbook();
            workbook.write(out);
            System.out.println("Ghi file thanh cong");
        }
    }

    public static void DangNhap(Login lg, ArrayList<TestCase> listTest, int i) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            // Check if logout element is present
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/nav/div/div/ul/li[4]")));
                listTest.add(new TestCase("Test case " + i, "fail"));
                System.out.println("User is already logged in.");
            } catch (TimeoutException e) {
                performLogin(lg, i);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            listTest.add(new TestCase("Test case " + i, "fail"));
        }
    }

    private static void performLogin(Login lg, int testCaseIndex) {
    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement elementEmail = driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div[1]/input"));
            elementEmail.sendKeys(lg.getEmail());

            WebElement elementPassword = driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div[2]/input"));
            elementPassword.sendKeys(lg.getPassword());

            WebElement elementLogin = driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div[3]/button"));
            elementLogin.click();

            try {
                wait.until(ExpectedConditions.alertIsPresent());
                String alertText = driver.switchTo().alert().getText();
                driver.switchTo().alert().accept();

                if (lg.getMessage().equals(alertText)) {
                    listTest.add(new TestCase("Test case " + testCaseIndex, "pass"));
                } else {
                    listTest.add(new TestCase("Test case " + testCaseIndex, "fail"));
                }
            } catch (TimeoutException e) {
                listTest.add(new TestCase("Test case " + testCaseIndex, "fail"));
            }
        } catch (NoSuchElementException e) {
            System.out.println("NoSuchElementException: " + e.getMessage());
            listTest.add(new TestCase("Test case " + testCaseIndex, "fail"));
        } finally {
        	try {
                WebElement elementEmail = driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div[1]/input"));
                WebElement elementPassword = driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div[2]/input"));
                elementEmail.clear();
                elementPassword.clear();
            } catch (NoSuchElementException e) {
                System.out.println("Error clearing input fields: " + e.getMessage());
            }
        }
    }
}
