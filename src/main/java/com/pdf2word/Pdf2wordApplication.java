package com.pdf2word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import net.sourceforge.tess4j.*;

@SpringBootApplication
public class Pdf2wordApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Pdf2wordApplication.class, args);
		List<String> pdfs = new ArrayList<String>();
		File currentDir = new File(".");
		System.out.println("I will attempt to convert these files:");
		for (File file : currentDir.listFiles()) {

			if (!file.isDirectory() && file.getPath().endsWith(".pdf")) {
				pdfs.add(file.getPath().substring(2));
				System.out.println(file.getPath());
			}
		}


		// Open the pdf file
		for (String pdf : pdfs) {
			XWPFDocument doc = new XWPFDocument();
			System.out.println("\nProcessing.........");
			long a = Calendar.getInstance().getTimeInMillis();
			File imageFile = new File(pdf);
			Tesseract instance = new Tesseract(); // JNA Interface Mapping
			// Tesseract1 instance = new Tesseract1(); // JNA Direct Mapping

			try {
				String result = instance.doOCR(imageFile);
				XWPFParagraph p = doc.createParagraph();
				XWPFRun run = p.createRun();
				run.setText(result);
				// Adding a page break
			} catch (TesseractException e) {
				System.err.println(e.getMessage());
			}
			// Write the word document

			FileOutputStream out = new FileOutputStream("output_" + pdf.substring(0, pdf.length() - 4) + ".docx");
			doc.write(out);
			// Close all open files
			out.close();
			doc.close();
			long b = Calendar.getInstance().getTimeInMillis() - a;
			double c = b / 1000;
			c = c / 60;
			System.out.println("Conversion completed for : " + pdf + ". Please check the file:" + "output_"
					+ pdf.substring(0, pdf.length() - 4) + ".docx" + "\n" + "It took " + c
					+ " minutes to process this file");
		}

	}
}
