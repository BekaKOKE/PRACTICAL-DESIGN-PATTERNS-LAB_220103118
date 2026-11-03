package task1;

public class CsvFormatter implements ReportFormatter {

    @Override
    public String format(String title, String[] headers, String[][] rows) {
        StringBuilder sb = new StringBuilder();
        sb.append("# TITLE: ").append(title).append("\n");
        sb.append(toLine(headers)).append("\n");
        for (String[] row : rows) {
            sb.append(toLine(row)).append("\n");
        }
        return sb.toString();
    }

    private String toLine(String[] values) {
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            String v = values[i] == null ? "" : values[i];
            line.append("\"").append(v.replace("\"", "\"\"")).append("\"");
            if (i < values.length - 1) line.append(",");
        }
        return line.toString();
    }
}
