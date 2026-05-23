package HomeTask;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// ─────────────────────────────────────────────────────────────────────────────
// Custom Exception Classes
// ─────────────────────────────────────────────────────────────────────────────

class EmptyFieldException extends Exception {
	public EmptyFieldException(String fieldName) {
		super("Required field is empty: " + fieldName);
	}
}

class InvalidRollNumberException extends Exception {
	public InvalidRollNumberException(String rollNumber) {
		super("Invalid Roll Number: \"" + rollNumber + "\"\nExpected format: i.e: L1F23BSSE0184  (Level + Semester + Year + Program + Roll)");
	}
}

class InvalidDateException extends Exception {
	public InvalidDateException() {
		super("Return date must be after the issue date.");
	}
}

class NullSelectionException extends Exception {
	public NullSelectionException(String fieldName) {
		super("No selection made for: " + fieldName);
	}
}

// Mandatory user-defined custom exception
class BookAlreadyIssuedException extends Exception {
	public BookAlreadyIssuedException(String bookTitle) {
		super("\"" + bookTitle + "\" is already issued and not available.");
	}
}

// ─────────────────────────────────────────────────────────────────────────────
// Main GUI Class
// ─────────────────────────────────────────────────────────────────────────────

public class HomeTaskGui {

	private JFrame frame;

	private JTextField txtStudentName;
	private JTextField txtRollNumber;
	private JTextField txtBookTitle;
	private JTextField txtIssueDate;
	private JTextField txtReturnDate;
	private JTextArea txtAreaRemarks;
	private JComboBox<String> cmbBookCategory;
	private JRadioButton rdoNewEdition;
	private JRadioButton rdoOldEdition;

	// Simulates a book already issued in the system
	private String issuedBook = null;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HomeTaskGui window = new HomeTaskGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public HomeTaskGui() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Library Book Issue System");
		frame.setBounds(100, 100, 544, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(new Color(240, 244, 255));

		// ── Title ─────────────────────────────────────────────────────────────
		JLabel lblTitle = new JLabel("Library Book Issue System");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTitle.setForeground(new Color(25, 55, 120));
		lblTitle.setBounds(160, 10, 280, 20);
		frame.getContentPane().add(lblTitle);

		// ── LEFT COLUMN ───────────────────────────────────────────────────────

		JLabel lblStudentName = new JLabel("Student Name:");
		lblStudentName.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblStudentName.setBounds(20, 50, 110, 25);
		frame.getContentPane().add(lblStudentName);

		txtStudentName = new JTextField();
		txtStudentName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtStudentName.setBounds(20, 75, 200, 25);
		frame.getContentPane().add(txtStudentName);

		JLabel lblRollNumber = new JLabel("Roll Number:");
		lblRollNumber.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRollNumber.setBounds(20, 115, 110, 25);
		frame.getContentPane().add(lblRollNumber);

		txtRollNumber = new JTextField();
		txtRollNumber.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtRollNumber.setBounds(20, 140, 200, 25);
		frame.getContentPane().add(txtRollNumber);

		JLabel lblBookCategory = new JLabel("Book Category:");
		lblBookCategory.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBookCategory.setBounds(20, 180, 110, 25);
		frame.getContentPane().add(lblBookCategory);

		String[] categories = { "-- Select --", "Programming", "AI", "Databases", "Networking" };
		cmbBookCategory = new JComboBox<>(categories);
		cmbBookCategory.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cmbBookCategory.setBounds(20, 205, 200, 25);
		frame.getContentPane().add(cmbBookCategory);

		JLabel lblBookEdition = new JLabel("Book Edition:");
		lblBookEdition.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBookEdition.setBounds(20, 245, 110, 25);
		frame.getContentPane().add(lblBookEdition);

		rdoNewEdition = new JRadioButton("New Edition");
		rdoNewEdition.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdoNewEdition.setBackground(new Color(240, 244, 255));
		rdoNewEdition.setBounds(20, 268, 110, 25);
		rdoNewEdition.setSelected(true);
		frame.getContentPane().add(rdoNewEdition);

		rdoOldEdition = new JRadioButton("Old Edition");
		rdoOldEdition.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdoOldEdition.setBackground(new Color(240, 244, 255));
		rdoOldEdition.setBounds(130, 268, 110, 25);
		frame.getContentPane().add(rdoOldEdition);

		ButtonGroup editionGroup = new ButtonGroup();
		editionGroup.add(rdoNewEdition);
		editionGroup.add(rdoOldEdition);

		// ── RIGHT COLUMN ──────────────────────────────────────────────────────

		JLabel lblBookTitle = new JLabel("Book Title:");
		lblBookTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblBookTitle.setBounds(290, 50, 110, 25);
		frame.getContentPane().add(lblBookTitle);

		txtBookTitle = new JTextField();
		txtBookTitle.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtBookTitle.setBounds(290, 75, 200, 25);
		frame.getContentPane().add(txtBookTitle);

		JLabel lblIssueDate = new JLabel("Issue Date (dd-MM-yyyy):");
		lblIssueDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblIssueDate.setBounds(290, 115, 200, 25);
		frame.getContentPane().add(lblIssueDate);

		txtIssueDate = new JTextField();
		txtIssueDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtIssueDate.setBounds(290, 140, 200, 25);
		frame.getContentPane().add(txtIssueDate);

		JLabel lblReturnDate = new JLabel("Return Date (dd-MM-yyyy):");
		lblReturnDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblReturnDate.setBounds(290, 180, 200, 25);
		frame.getContentPane().add(lblReturnDate);

		txtReturnDate = new JTextField();
		txtReturnDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtReturnDate.setBounds(290, 205, 200, 25);
		frame.getContentPane().add(txtReturnDate);

		JLabel lblRemarks = new JLabel("Remarks (optional):");
		lblRemarks.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblRemarks.setBounds(290, 245, 140, 25);
		frame.getContentPane().add(lblRemarks);

		txtAreaRemarks = new JTextArea();
		txtAreaRemarks.setFont(new Font("Tahoma", Font.PLAIN, 12));
		JScrollPane scrollRemarks = new JScrollPane(txtAreaRemarks);
		scrollRemarks.setBounds(290, 268, 200, 50);
		frame.getContentPane().add(scrollRemarks);

		// ── BUTTONS ───────────────────────────────────────────────────────────

		JButton btnIssueBook = new JButton("Issue Book");
		btnIssueBook.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnIssueBook.setBackground(new Color(60, 63, 65));
		btnIssueBook.setForeground(Color.WHITE);
		btnIssueBook.setFocusPainted(false);
		btnIssueBook.setBounds(80, 340, 110, 28);
		frame.getContentPane().add(btnIssueBook);

		JButton btnReset = new JButton("Reset");
		btnReset.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnReset.setBackground(new Color(60, 63, 65));
		btnReset.setForeground(Color.WHITE);
		btnReset.setFocusPainted(false);
		btnReset.setBounds(205, 340, 100, 28);
		frame.getContentPane().add(btnReset);

		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnExit.setBackground(new Color(60, 63, 65));
		btnExit.setForeground(Color.WHITE);
		btnExit.setFocusPainted(false);
		btnExit.setBounds(320, 340, 100, 28);
		frame.getContentPane().add(btnExit);

		// ── ACTION LISTENERS ──────────────────────────────────────────────────

		btnIssueBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					String studentName      = txtStudentName.getText().trim();
					String rollNumber       = txtRollNumber.getText().trim();
					String bookTitle        = txtBookTitle.getText().trim();
					String selectedCategory = (String) cmbBookCategory.getSelectedItem();
					String issueDate        = txtIssueDate.getText().trim();
					String returnDate       = txtReturnDate.getText().trim();
					String selectedEdition  = rdoNewEdition.isSelected() ? "New Edition" : "Old Edition";
					String remarks          = txtAreaRemarks.getText().trim();

					// EmptyFieldException
					if (studentName.isEmpty())
						throw new EmptyFieldException("Student Name");
					if (rollNumber.isEmpty())
						throw new EmptyFieldException("Roll Number");
					if (bookTitle.isEmpty())
						throw new EmptyFieldException("Book Title");
					if (issueDate.isEmpty())
						throw new EmptyFieldException("Issue Date");
					if (returnDate.isEmpty())
						throw new EmptyFieldException("Return Date");

					// NullSelectionException
					if (selectedCategory.equals("-- Select --"))
						throw new NullSelectionException("Book Category");

					// InvalidRollNumberException — valid format: L1F23BSSE0184
					// Pattern: L + 1 digit (level) + 1 letter (semester) + 2 digits (year) + letters (program) + 4 digits (roll)
					if (!rollNumber.matches("[Ll]\\d[A-Za-z]\\d{2}[A-Za-z]+\\d{4}"))
						throw new InvalidRollNumberException(rollNumber);

					// NumberFormatException — parse the last 4 digit roll number
					String digitsPart = rollNumber.replaceAll(".*[A-Za-z](\\d{4})$", "$1");
					int rollDigits = Integer.parseInt(digitsPart);
					if (rollDigits <= 0)
						throw new NumberFormatException("Roll number digits must be positive.");

					// InvalidDateException — parse and compare dates
					DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
					LocalDate parsedIssueDate  = LocalDate.parse(issueDate, dateFormatter);
					LocalDate parsedReturnDate = LocalDate.parse(returnDate, dateFormatter);

					if (!parsedReturnDate.isAfter(parsedIssueDate))
						throw new InvalidDateException();

					// BookAlreadyIssuedException — mandatory custom exception
					if (issuedBook != null && bookTitle.equalsIgnoreCase(issuedBook))
					    throw new BookAlreadyIssuedException(bookTitle);
					issuedBook = bookTitle;
					// All validations passed — show success
					String confirmationMessage = "Book Issued Successfully!\n\n"
							+ "Student Name : " + studentName + "\n"
							+ "Roll Number  : " + rollNumber + "\n"
							+ "Book Title   : " + bookTitle + "\n"
							+ "Category     : " + selectedCategory + "\n"
							+ "Edition      : " + selectedEdition + "\n"
							+ "Issue Date   : " + issueDate + "\n"
							+ "Return Date  : " + returnDate + "\n"
							+ (remarks.isEmpty() ? "" : "Remarks      : " + remarks);

					JOptionPane.showMessageDialog(frame, confirmationMessage, "Success", JOptionPane.INFORMATION_MESSAGE);

				} catch (EmptyFieldException ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Empty Field", JOptionPane.WARNING_MESSAGE);

				} catch (NullSelectionException ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "No Selection", JOptionPane.WARNING_MESSAGE);

				} catch (InvalidRollNumberException ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Invalid Roll Number", JOptionPane.WARNING_MESSAGE);

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(frame, "Number Format Error: " + ex.getMessage(), "Number Format Error", JOptionPane.WARNING_MESSAGE);

				} catch (DateTimeParseException ex) {
					JOptionPane.showMessageDialog(frame, "Invalid date format.\nPlease use dd-MM-yyyy  (e.g. 14-05-2025).", "Invalid Date Format", JOptionPane.WARNING_MESSAGE);

				} catch (InvalidDateException ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Invalid Date", JOptionPane.WARNING_MESSAGE);

				} catch (BookAlreadyIssuedException ex) {
					JOptionPane.showMessageDialog(frame, ex.getMessage(), "Book Unavailable", JOptionPane.ERROR_MESSAGE);

				} finally {
					JOptionPane.showMessageDialog(frame, "Operation Completed.", "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtStudentName.setText("");
				txtRollNumber.setText("");
				txtBookTitle.setText("");
				txtIssueDate.setText("");
				txtReturnDate.setText("");
				txtAreaRemarks.setText("");
				cmbBookCategory.setSelectedIndex(0);
				rdoNewEdition.setSelected(true);
			}
		});

		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int userConfirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
				if (userConfirmation == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

	}
}