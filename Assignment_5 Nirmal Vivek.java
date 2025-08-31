/**
 * Assignment 5: Library Management System with Fine Calculation
 * Demonstrates a real-world application with business logic.
 *
 * Features:
 * - Book and Member classes
 * - Book issuing, returning, renewing, reserving
 * - Fine calculation for overdue books
 * - Multiple member types with different privileges
 * - Library-wide reports (overdue books, most popular, etc.)
 *
 * Author: StudentName
 */

import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private boolean isIssued;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private int timesIssued;

    public Book(String bookId, String title, String author, String isbn, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.isIssued = false;
        this.timesIssued = 0;
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public boolean isIssued() { return isIssued; }
    public LocalDate getDueDate() { return dueDate; }
    public int getTimesIssued() { return timesIssued; }

    public void issueBook(int days) {
        this.isIssued = true;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(days);
        this.timesIssued++;
    }

    public void returnBook() {
        this.isIssued = false;
        this.issueDate = null;
        this.dueDate = null;
    }

    public void renewBook(int days) {
        if (isIssued) {
            this.dueDate = LocalDate.now().plusDays(days);
        }
    }

    @Override
    public String toString() {
        return "[" + bookId + "] " + title + " by " + author + (isIssued ? " (Issued, Due: " + dueDate + ")" : " (Available)");
    }
}

class Member {
    private String memberId;
    private String memberName;
    private String memberType; // Student, Faculty, General
    private List<Book> booksIssued;
    private double totalFines;
    private LocalDate membershipDate;

    // Static variables
    private static int totalMembers = 0;
    private static String libraryName = "Central Library";
    private static double finePerDay = 5.0;
    private static int maxBooksAllowed = 3; // default

    public Member(String memberName, String memberType) {
        this.memberId = "MEM" + String.format("%03d", ++totalMembers);
        this.memberName = memberName;
        this.memberType = memberType;
        this.booksIssued = new ArrayList<>();
        this.totalFines = 0.0;
        this.membershipDate = LocalDate.now();

        // Set borrowing privileges based on type
        if (memberType.equalsIgnoreCase("Student")) maxBooksAllowed = 3;
        else if (memberType.equalsIgnoreCase("Faculty")) maxBooksAllowed = 5;
        else if (memberType.equalsIgnoreCase("General")) maxBooksAllowed = 2;
    }

    public String getMemberName() { return memberName; }
    public double getTotalFines() { return totalFines; }
    public List<Book> getBooksIssued() { return booksIssued; }

    public void issueBook(Book book, int days) {
        if (booksIssued.size() >= maxBooksAllowed) {
            System.out.println(memberName + " cannot issue more than " + maxBooksAllowed + " books.");
            return;
        }
        if (book.isIssued()) {
            System.out.println("Book already issued: " + book.getTitle());
            return;
        }
        book.issueBook(days);
        booksIssued.add(book);
        System.out.println(memberName + " issued book: " + book.getTitle());
    }

    public void returnBook(Book book) {
        if (!booksIssued.contains(book)) {
            System.out.println(memberName + " has not issued this book.");
            return;
        }
        calculateFine(book);
        book.returnBook();
        booksIssued.remove(book);
        System.out.println(memberName + " returned book: " + book.getTitle());
    }

    public void renewBook(Book book, int days) {
        if (booksIssued.contains(book)) {
            book.renewBook(days);
            System.out.println(memberName + " renewed book: " + book.getTitle());
        }
    }

    private void calculateFine(Book book) {
        if (book.getDueDate() != null && LocalDate.now().isAfter(book.getDueDate())) {
            long overdueDays = Duration.between(book.getDueDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();
            double fine = overdueDays * finePerDay;
            totalFines += fine;
            System.out.println("Fine for " + memberName + ": Rs." + fine + " (Overdue " + overdueDays + " days)");
        }
    }

    public static void searchBooks(List<Book> catalog, String keyword) {
        System.out.println("Search Results for \"" + keyword + "\":");
        for (Book b : catalog) {
            if (b.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                b.getBookId().equalsIgnoreCase(keyword)) {
                System.out.println(b);
            }
        }
    }

    public static void reserveBook(Book book) {
        if (book.isIssued()) {
            System.out.println("Book " + book.getTitle() + " reserved. Will notify when available.");
        } else {
            System.out.println("Book is available, no need to reserve.");
        }
    }

    // ---------- Static Reports ----------
    public static void generateLibraryReport(List<Book> catalog, List<Member> members) {
        System.out.println("\n--- Library Report (" + libraryName + ") ---");
        System.out.println("Total Members: " + totalMembers);
        System.out.println("Total Books: " + catalog.size());

        System.out.println("\nMembers and Fines:");
        for (Member m : members) {
            System.out.println(m.memberName + " | Books Issued: " + m.booksIssued.size() + " | Total Fines: Rs." + m.totalFines);
        }

        System.out.println("\nMost Popular Books:");
        getMostPopularBooks(catalog, 3);
        System.out.println("------------------------------\n");
    }

    public static void getOverdueBooks(List<Book> catalog) {
        System.out.println("\n--- Overdue Books ---");
        LocalDate today = LocalDate.now();
        for (Book b : catalog) {
            if (b.isIssued() && b.getDueDate().isBefore(today)) {
                System.out.println(b);
            }
        }
    }

    public static void getMostPopularBooks(List<Book> catalog, int top) {
        catalog.stream()
                .sorted((b1, b2) -> Integer.compare(b2.getTimesIssued(), b1.getTimesIssued()))
                .limit(top)
                .forEach(b -> System.out.println(b.getTitle() + " (Issued " + b.getTimesIssued() + " times)"));
    }

    // ---------- Static Config ----------
    public static void setFinePerDay(double fine) { finePerDay = fine; }
    public static void setLibraryName(String name) { libraryName = name; }
}

public class Assignment5_StudentName {
    public static void main(String[] args) {
        // Setup
        Member.setLibraryName("City Central Library");
        Member.setFinePerDay(10.0);

        List<Book> catalog = new ArrayList<>();
        catalog.add(new Book("B001", "Java Programming", "James Gosling", "12345", "Programming"));
        catalog.add(new Book("B002", "Database Systems", "C.J. Date", "67890", "Databases"));
        catalog.add(new Book("B003", "Operating Systems", "Silberschatz", "11223", "Systems"));

        List<Member> members = new ArrayList<>();
        Member m1 = new Member("Alice", "Student");
        Member m2 = new Member("Bob", "Faculty");
        members.add(m1);
        members.add(m2);

        // Operations
        m1.issueBook(catalog.get(0), 7);
        m2.issueBook(catalog.get(1), 5);

        Member.searchBooks(catalog, "Java");
        Member.reserveBook(catalog.get(0));

        // Simulate return after delay
        m1.returnBook(catalog.get(0)); 
        m2.renewBook(catalog.get(1), 10);

        // Reports
        Member.getOverdueBooks(catalog);
        Member.generateLibraryReport(catalog, members);
    }
}
