package interview.googleapis;


import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.Getter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class GoogleSheetApi extends GoogleApi {
    protected final Sheets sheet;

    public GoogleSheetApi(String credentialsPath) {
        this.sheet = GoogleApi.getSheetsService(credentialsPath);
    }

    /**
     * Creates a new Google Spreadsheet with a specified title.
     *
     * @param sheetTitle Title of the new spreadsheet.
     * @return The ID of the newly created spreadsheet.
     * @throws IOException If there is an error during creation.
     */
    public String createSpreadSheet(String sheetTitle) throws IOException {
        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                        .setTitle(sheetTitle));
        spreadsheet = this.sheet
                .spreadsheets()
                .create(spreadsheet)
                .setFields("spreadsheetId")
                .execute();
        return spreadsheet.getSpreadsheetId();
    }

    /**
     * Retrieves values from a specified range in the Google Spreadsheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param range         The range of cells to retrieve.
     * @return A ValueRange object containing the values of the cells.
     * @throws IOException If there is an error during data retrieval.
     */
    public ValueRange getValues(String spreadsheetId, String range) throws IOException {
        return sheet
                .spreadsheets()
                .values()
                .get(spreadsheetId, range)
                .execute();
    }


    /**
     * Retrieves values as a table (List of List Objects) from a specified range in the spreadsheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param range         The range of cells to retrieve.
     * @return A list of rows, each containing a list of cell values.
     * @throws IOException If there is an error during data retrieval.
     */
    public List<List<Object>> getValuesByTable(String spreadsheetId, String range) throws IOException {
        return getValues(spreadsheetId, range).getValues();
    }

    /**
     * Updates values in a specified range of the spreadsheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param range         The range of cells to update.
     * @param newValues     The new values to be placed in the specified range.
     * @param options       Input options (e.g., "RAW" or "USER_ENTERED").
     * @throws IOException If there is an error during the update.
     */
    public void updateValue(String spreadsheetId, String range, List<List<Object>> newValues, Options options) throws IOException {
        ValueRange body = new ValueRange().setValues(newValues);
        sheet.spreadsheets().values()
                .update(spreadsheetId, range, body)
                .setValueInputOption(options.toString())
                .execute();
    }

    public void appendValue(String spreadsheetId, String range, List<List<Object>> newValues, Options options) throws IOException {
        ValueRange body = new ValueRange().setValues(newValues);
        sheet.spreadsheets().values()
                .append(spreadsheetId, range, body)
                .setValueInputOption(options.toString())
                .execute();
    }

    /**
     * Freeze the specific row
     *
     * @param spreadsheetId
     * @param sheetName
     * @param frozenRow
     * @throws IOException
     */
    public void freezeRow(String spreadsheetId, String sheetName, int frozenRow) throws IOException {
        List<Request> requests = new ArrayList<>();
        requests.add(new Request().setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
                .setProperties(new SheetProperties().setSheetId(getSheetId(spreadsheetId, sheetName))
                        .setGridProperties(new GridProperties().setFrozenRowCount(frozenRow)))
                .setFields("gridProperties.frozenRowCount")));
        BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        sheet.spreadsheets().batchUpdate(spreadsheetId, batchRequest).execute();
    }

    /**
     * Retrieves the names of all sheets in a spreadsheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @return A list of sheet names.
     * @throws IOException If there is an error during data retrieval.
     */
    public List<String> getAllSheetName(String spreadsheetId) throws IOException {
        Spreadsheet spreadsheet = sheet.spreadsheets().get(spreadsheetId).execute();
        List<String> sheetNames = new ArrayList<>();
        for (Sheet sheet : spreadsheet.getSheets()) {
            sheetNames.add(sheet.getProperties().getTitle());
        }
        return sheetNames;
    }

    /**
     * Adds a new sheet with the specified name to an existing spreadsheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param sheetName     The name of the new sheet.
     * @return The ID of the newly created sheet.
     * @throws IOException If there is an error during the addition of the sheet.
     */
    public String addSheet(String spreadsheetId, String sheetName) throws IOException {
        AddSheetRequest addSheetRequest = new AddSheetRequest()
                .setProperties(new SheetProperties().setTitle(sheetName));

        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request().setAddSheet(addSheetRequest)));
        BatchUpdateSpreadsheetResponse response = sheet.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
        Integer sheetId = 0;
        for (Response res : response.getReplies()) {
            if (res.getAddSheet() != null && res.getAddSheet().getProperties() != null) {
                sheetId = res.getAddSheet().getProperties().getSheetId();
                break;
            }
        }
        return sheetId.toString();
    }

    /**
     * Checks if a sheet exists in the spreadsheet by its name.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param sheetName     The name of the sheet to check.
     * @return True if the sheet exists, otherwise false.
     * @throws IOException If there is an error during data retrieval.
     */
    public boolean isSheetExist(String spreadsheetId, String sheetName) throws IOException {
        List<String> sheets = getAllSheetName(spreadsheetId);
        return sheets.contains(sheetName);
    }

    /**
     * Retrieves the sheet ID based on the sheet name.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param sheetName     The name of the sheet.
     * @return The ID of the sheet.
     * @throws IOException If the sheet name is not found or there is an error during data retrieval.
     */
    public int getSheetId(String spreadsheetId, String sheetName) {
        try {
            Spreadsheet spreadsheet = sheet.spreadsheets().get(spreadsheetId).execute();
            for (Sheet sheet : spreadsheet.getSheets()) {
                if (sheet.getProperties().getTitle().equals(sheetName)) {
                    return sheet.getProperties().getSheetId();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalArgumentException("Sheet name not found" + sheetName);
    }

    public List<CellPosition> findsCellPosition(String spreadsheetId, String sheetName, String value) throws IOException {
        System.out.println(">> find cell position for row domain: " + value);
        List<List<Object>> values = getValuesByTable(spreadsheetId, sheetName + "!A1:M");
        List<CellPosition> cellsPosition = new ArrayList<>();
        for (int row = 0; row < values.size(); row++) {
            List<Object> rowValues = values.get(row);
            for (int col = 0; col < rowValues.size(); col++) {
                if (rowValues.get(col).equals(value)) {
                    cellsPosition.add(new CellPosition(row, col));
                }
            }
        }
        return cellsPosition;
    }

    public void setBackgroundColorOfCellByIndex(String spreadsheetId, String sheetName, int row, int col, CellColor color) {
        CellFormat cellFormat = new CellFormat()
                .setBackgroundColor(new Color()
                        .setRed(color.getRed())
                        .setGreen(color.getGreen())
                        .setBlue(color.getBlue())
                        .setAlpha(color.getAlpha()));
        try {
            RepeatCellRequest repeatCellRequest = new RepeatCellRequest()
                    .setRange(new GridRange().setSheetId(getSheetId(spreadsheetId, sheetName))
                            .setStartRowIndex(row)
                            .setEndRowIndex(row + 1)
                            .setStartColumnIndex(col)
                            .setEndColumnIndex(col + 1))
                    .setCell(new CellData().setUserEnteredFormat(cellFormat))
                    .setFields("userEnteredFormat.backgroundColor");
            BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                    .setRequests(Collections.singletonList(new Request().setRepeatCell(repeatCellRequest)));
            sheet.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<CellPosition> getCellPositions(String spreadsheetId, String sheetName, String domain) throws IOException {
        return findsCellPosition(spreadsheetId, sheetName, domain);
    }

    public String getCellAddress(int row, int col) {
        String colLetter = String.valueOf((char) ('A' + col));
        return colLetter + (row + 1);
    }

    /**
     * Renames a sheet in the spreadsheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param title         The current title of the sheet.
     * @param newTitle      The new title to assign to the sheet.
     * @throws IOException If there is an error during the renaming process.
     */
    public void renameSheetTitle(String spreadsheetId, String title, String newTitle) throws IOException {
        if (!isSheetExist(spreadsheetId, title)) {
            System.out.println("Sheet name not found " + title);
            return;
        }
        int sheetId = getSheetId(spreadsheetId, title);
        System.out.println("Renaming sheet. Sheet ID: " + sheetId);
        Request request = new Request()
                .setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
                        .setProperties(new SheetProperties()
                                .setSheetId(sheetId)
                                .setTitle(newTitle))
                        .setFields("title"));
        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(request));
        sheet.spreadsheets().batchUpdate(spreadsheetId, body).execute();
    }


    /**
     * Clears the values in a specified range of the sheet.
     *
     * @param spreadId The ID of the spreadsheet.
     * @param range    The range of cells to clear (e.g., "Sheet1!A1:D10").
     * @throws IOException If there is an error during the clearing process.
     */
    public void clearValue(String spreadId, String range) throws IOException {
        ClearValuesRequest requestBody = new ClearValuesRequest();
        Sheets.Spreadsheets.Values.Clear request =
                sheet.spreadsheets().values().clear(spreadId, range, requestBody);
        request.execute();
    }

    /**
     * Downloads a sheet as a CSV file.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param sheetGid      The gid of the sheet to download.
     * @param outputPath    The local path where the CSV file should be saved.
     */
    public void downloadSheetByGid(String spreadsheetId, String sheetGid, String outputPath, String range) {
        if (!sheetGid.isBlank()) {
            String _range = "";
            if (!range.isEmpty()) {
                _range = "&range=" + range;
            }
            String exportUrl = "https://docs.google.com/spreadsheets/d/" + spreadsheetId + "/export?format=csv&gid=" + sheetGid + _range;

            try {
                HttpResponse httpResponse = sheet.getRequestFactory()
                        .buildGetRequest(new GenericUrl(exportUrl))
                        .execute();

                try (FileOutputStream fileOutputStream = new FileOutputStream(outputPath)) {
                    httpResponse.download(fileOutputStream);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Downloads a sheet as a CSV file.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param sheetName     The name of the sheet to download.
     * @param outputPath    The local path where the CSV file should be saved.
     */
    public void downloadSheetByName(String spreadsheetId, String sheetName, String outputPath) {
        downloadSheetByName(spreadsheetId, sheetName, outputPath, "");
    }

    public void downloadSheetByName(String spreadsheetId, String sheetName, String outputPath, String range) {
        String sheetGid = String.valueOf(getSheetId(spreadsheetId, sheetName));
        downloadSheetByGid(spreadsheetId, sheetGid, outputPath, range);
    }

    /**
     * Retrieves all values from a sheet.
     *
     * @param spreadId  The ID of the spreadsheet.
     * @param sheetName The name of the sheet.
     * @return A list of rows, each containing a list of cell values.
     */
    public List<List<Object>> getAllValueFromSheet(String spreadId, String sheetName) {
        return getValuesFromSheet(spreadId, sheetName);
    }

    /**
     * Retrieves values from a specified range in the sheet.
     *
     * @param spreadId The ID of the spreadsheet.
     * @param range    The range of cells to retrieve.
     * @return A list of rows, each containing a list of cell values.
     */
    public List<List<Object>> getValuesFromSheet(String spreadId, String range) {
        ValueRange response;
        try {
            response = sheet.spreadsheets().values()
                    .get(spreadId, range)
                    .execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response.getValues();
    }


    /**
     * Gets the first empty row in a sheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param range         The range to check for empty rows.
     * @return The index of the first empty row (1-based).
     * @throws IOException If there is an error during data retrieval.
     */
    public int getFirstEmptyRow(String spreadsheetId, String range) throws IOException {
        List<List<Object>> values = getValuesFromSheet(spreadsheetId, range);
        if (values == null || values.isEmpty()) {
            return 1;
        } else {
            return values.size() + 1;
        }
    }

    /**
     * Retrieves the last row that contains data in a sheet.
     *
     * @param spreadsheetId The ID of the spreadsheet.
     * @param range         The range to check.
     * @return The index of the last row that contains data (1-based).
     * @throws IOException If there is an error during data retrieval.
     */
    public int getLastValuesRow(String spreadsheetId, String range) throws IOException {
        int emptyRow = getFirstEmptyRow(spreadsheetId, range);
        if (emptyRow > 1) {
            return emptyRow - 1;
        }
        return emptyRow;
    }

    /**
     * Convert number index to column name
     *
     * @param index
     * @return
     */
    public static String indexToColumnName(int index) {
        StringBuilder columnName = new StringBuilder();

        while (index >= 0) {
            int remainder = index % 26;
            columnName.insert(0, (char) (remainder + 'A'));
            index = (index / 26) - 1;
        }

        return columnName.toString();
    }
}
