package org.cayman.utils;


import org.cayman.dto.Author;
import org.cayman.dto.Book;
import org.cayman.dto.BookDto;
import org.cayman.dto.Rating;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class EntityConverter {
    public static List<BookDto> convertBookToBookDTO(String fileServiceUrl, Book ... books) {
        List<BookDto> result = new ArrayList<>();
        for (Book book : books) {
            result.add(BookDto.builder()
                    .id(book.getId())
                    .name(book.getName())
                    .language(book.getLanguage())
                    .year(book.getYear())
                    .author(convertAuthors(book.getAuthor()))
                    .publisher(book.getPublisher().getName())
                    .image(fileServiceUrl + book.getImage())
                    .fileId(book.getFileId())
                    .description(book.getDescription())
                    .categoryId(book.getCategory().getId())
                    .category(book.getCategory().getName())
                    .pageCount(book.getPageCount())
                    .rating(extractRating(book.getRating()))
                    .views(book.getViews())
                    .addDate(book.getAddDate())
                    .build()
            );
        }
        return result;
    }

    private static String extractRating(Rating rating) {
        if (rating.getVotes() != 0) {
            return new BigDecimal(rating.getPoints() / rating.getVotes())
                            .setScale(2, RoundingMode.UP).toString();
        }
        return "0";
    }

    private static String convertAuthors(List<Author> authors) {
        StringBuilder sb = new StringBuilder();
        for (Author author : authors) {
            sb.append(author.getName());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}
