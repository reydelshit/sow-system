
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Reydel
 */
public class DOWNLOAD implements Printable {

    private final JTable[] tables;
    private final String eartag;
    private final int EARTAG_HEIGHT = 20; // Adjust the value as needed

    public DOWNLOAD(String eartag, JTable... tables) {
        this.eartag = eartag;
        this.tables = tables;
    }

    private void printEartag(Graphics2D g2d, String eartag, PageFormat pageFormat) {
        Font eartagFont = new Font("Arial", Font.BOLD, 12);
        g2d.setFont(eartagFont);
        FontMetrics fm = g2d.getFontMetrics();
        int pageWidth = (int) pageFormat.getImageableWidth();
        int eartagWidth = fm.stringWidth("Eartag: " + eartag);
        int x = (pageWidth - eartagWidth) / 2;
        int y = fm.getAscent() + 10; // Adjust the vertical position as desired
        g2d.drawString("Eartag: " + eartag, x, y);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0 || tables.length == 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int yOffset = EARTAG_HEIGHT;

        JTable firstTable = tables[0];
        Font originalFont = firstTable.getFont();
        Font smallFont = originalFont.deriveFont(Font.PLAIN, 8f); // Adjust the font size as desired
        firstTable.setFont(smallFont);

        for (JTable table : tables) {
            printEartag(g2d, eartag, pageFormat);

            JTableHeader header = table.getTableHeader();
            header.print(graphics);

            g2d.translate(0, header.getHeight());

            table.print(graphics);

            yOffset += header.getHeight() + table.getHeight();
            g2d.translate(0, yOffset);
        }

        firstTable.setFont(originalFont);

        return PAGE_EXISTS;
    }

}
