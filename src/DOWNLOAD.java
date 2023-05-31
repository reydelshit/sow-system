
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.text.Document;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Reydel
 */
public class DOWNLOAD implements Printable {

    private List<JTable> tables;

    public void printTablesToPdf(List<JTable> tables, String eartag) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new DOWNLOAD(tables, eartag));

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    private String eartag;

    public DOWNLOAD(List<JTable> tables, String eartag) {
        this.tables = tables;
        this.eartag = eartag;
    }

   @Override
public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
    if (pageIndex >= tables.size()) {
        return NO_SUCH_PAGE;
    }

    Graphics2D g2d = (Graphics2D) graphics;
    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

    JTable table = tables.get(pageIndex);
    String tableTitle = ""; // Initialize with an empty title

    // Set the title based on the table index
    if (pageIndex == 0) {
        tableTitle = "FARROWING";
    } else if (pageIndex == 1) {
        tableTitle = "BREEDING";
    } else if (pageIndex == 2) {
        tableTitle = "WEANING";
    }

    // Calculate the scaling factor to fit the table within the printable area
    double scaleX = pageFormat.getImageableWidth() / table.getWidth();
    double scaleY = pageFormat.getImageableHeight() / table.getHeight();
    double scale = Math.min(scaleX, scaleY);

    // Apply the scaling transformation
    g2d.scale(scale, scale);

    Font originalFont = g2d.getFont();
    Font titleFont = originalFont.deriveFont(Font.BOLD, 24);
    g2d.setFont(titleFont);

    // Print the table title
    g2d.drawString(tableTitle, 0, g2d.getFontMetrics().getHeight());

    // Print the eartag
    if (pageIndex == 0) {
        g2d.setFont(originalFont); // Restore the original font
        String eartagText = "Eartag: " + eartag;
        Font originalFontForEartag = g2d.getFont();
        Font eartagFont = originalFontForEartag.deriveFont(Font.BOLD, 36); // Increase the font size to 36
        g2d.setFont(eartagFont);
        int eartagWidth = g2d.getFontMetrics().stringWidth(eartagText);
        int eartagX = (int) (pageFormat.getImageableWidth() - eartagWidth);
        g2d.drawString(eartagText, eartagX, 2 * g2d.getFontMetrics().getHeight());
    }

    // Print the table header
    JTableHeader tableHeader = table.getTableHeader();
    Rectangle headerRect = tableHeader.getBounds();
    headerRect.width = table.getWidth();
    g2d.translate(0, 2 * g2d.getFontMetrics().getHeight() + headerRect.height);
    tableHeader.print(g2d);

    // Adjust the printing area to exclude the header
    g2d.translate(0, headerRect.height);

    // Print the table data
    table.print(g2d);

    return PAGE_EXISTS;
}

//    private final JTable[] tables;
//    private final String eartag;
//    private final int EARTAG_HEIGHT = 20; // Adjust the value as needed
//
//    public DOWNLOAD(String eartag, JTable... tables) {
//        this.eartag = eartag;
//        this.tables = tables;
//    }
//    @Override
//    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
//        if (pageIndex > 0 || tables.length == 0) {
//            return NO_SUCH_PAGE;
//        }
//
//        Graphics2D g2d = (Graphics2D) graphics;
//        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
//
//        int yOffset = EARTAG_HEIGHT;
//
//        JTable firstTable = tables[0];
//        Font originalFont = firstTable.getFont();
//        Font smallFont = originalFont.deriveFont(Font.PLAIN, 8f); // Adjust the font size as desired
//        firstTable.setFont(smallFont);
//
//        for (JTable table : tables) {
//            printEartag(g2d, eartag, pageFormat);
//
//            JTableHeader header = table.getTableHeader();
//            header.print(graphics);
//
//            g2d.translate(0, header.getHeight());
//
//            table.print(graphics);
//
//            yOffset += header.getHeight() + table.getHeight();
//            g2d.translate(0, yOffset);
//        }
//
//        firstTable.setFont(originalFont);
//
//        return PAGE_EXISTS;
//    }
}
