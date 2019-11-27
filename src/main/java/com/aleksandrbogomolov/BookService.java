package com.aleksandrbogomolov;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.CollectionPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService implements CollectionPageService<BookCriteria, Book> {
    @Autowired
    private JdbcTemplate jdbc;

    public Integer create(Book book) {
        GeneratedKeyHolder idHolder = new GeneratedKeyHolder();
        jdbc.update(con -> con.prepareStatement(
                String.format("INSERT INTO book (name, author_id) VALUES ('%s', %d)",
                    book.getName(),
                    book.getAuthor().getId())),
                idHolder);
        return (Integer) idHolder.getKeyList().get(0).get("SCOPE_IDENTITY()");
    }

    public void update(Book book) {
        jdbc.update("UPDATE book SET " +
                        "name = ?, " +
                        "author_id = ? " +
                        "WHERE id = ?",
                book.getName(),
                book.getAuthor().getId(),
                book.getId());
    }
    public void delete(Integer id) {
        jdbc.update("DELETE FROM book WHERE id = ?", id);
    }


    @Override
    public CollectionPage<Book> getCollectionPage(BookCriteria bookCriteria) {

        List<String> f = new ArrayList<>();
        if (bookCriteria.getId() != null)
            f.add("b.id = " + bookCriteria.getId());
        if (bookCriteria.getName() != null)
            f.add("b.name like '%'||'" + bookCriteria.getName().trim()+ "'||'%'");
        if (bookCriteria.getAuthorId() != null)
            f.add("b.author_id = " + bookCriteria.getAuthorId());
        if (f.isEmpty())
            f.add("1=1");
        String filters = f.stream().reduce((a, b) -> a + " AND " + b).orElse(f.get(0));
        String orders = getSort(bookCriteria);
        List<Book> list = jdbc.query("SELECT b.id, b.name, a.id, a.name FROM book b JOIN author a ON a.id = b.author_id " +
                        "WHERE " + filters + orders,
                (resultSet, i) -> {
                    Book book = new Book();
                    book.setId(resultSet.getInt(1));
                    book.setName(resultSet.getString(2));
                    book.setAuthor(new Author());
                    book.getAuthor().setId(resultSet.getInt(3));
                    book.getAuthor().setName(resultSet.getString(4));
                    return book;
                });
        int end = bookCriteria.getFirst() + bookCriteria.getSize();
        return new CollectionPage<>(list.size(),
                list.subList(
                        bookCriteria.getFirst(),
                        list.size() < end ? list.size() : end),
                bookCriteria);
    }

    private String getSort(BookCriteria bookCriteria) {
        String orders = "";
        if (bookCriteria.getSorting() != null) {
            String field = null;
            switch (bookCriteria.getSorting().getField()) {
                case "name":
                    field = "b.name";
                    break;
                case "author.name":
                    field = "a.name";
                    break;
                default:
                    field = "b.id";
            }
            orders = " ORDER BY " + field + " " + bookCriteria.getSorting().getDirection().name();
        }
        return orders;
    }

    public void setJdbc(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
}
