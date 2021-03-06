package pl.ncdc.work.test.libraryapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.ncdc.work.test.libraryapp.model.Book;
import pl.ncdc.work.test.libraryapp.model.BookForm;
import pl.ncdc.work.test.libraryapp.service.BookService;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


@Controller
public class BookController {

    private final Logger logger = Logger.getLogger(BookController.class.getName());

    private BookService bookService;
    @Autowired
    public BookController(BookService bookService) {this.bookService = bookService; }

    private List<Book> books = new ArrayList<>();

    @GetMapping("/listPage")
    public String getAllBooks(Model model){

        logger.info("Getting books to listPage... going to service...");
        books = (List) bookService.getBooks();
        if (books.isEmpty()){
            logger.info("Redirecting to \"noRecords page\"");
            return "noRecordsFound";
        } else {
            model.addAttribute("books", books);
            logger.info("Books were founded. Redireting to listPage..");
            return "listPage";
        }

    }

    @GetMapping("/addPage")
    public String showAddBookPage(Model model) {
        BookForm bookForm = new BookForm();
        model.addAttribute("bookForm", bookForm);
        logger.info("Showing form of adding page..");
        return "addPage";
    }

    @PostMapping("/addPage")
    public String saveBook(@ModelAttribute("bookForm") BookForm bookForm) {
        Book book = new Book(
                bookForm.getTitle(),
                bookForm.getAuthor(),
                bookForm.getIsbn());

        if (bookService.saveBook(book)) {
            logger.info("Book: \n" + book.toString()+ "\nwas ADDed... \nRedirecting to listPage...");
            return "redirect:/listPage";
        } else {
            logger.info("Redirecting to error page...");
            return "uncorrectAuthor";
        }
    }



}
