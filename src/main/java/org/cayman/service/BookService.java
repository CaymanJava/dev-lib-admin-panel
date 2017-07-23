package org.cayman.service;

import lombok.extern.slf4j.Slf4j;
import org.cayman.dto.*;
import org.cayman.exception.SaveEntityException;
import org.cayman.utils.Constants;
import org.cayman.utils.Translit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.cayman.utils.EntityConverter.convertBookToBookDTO;

@Controller
@Slf4j
public class BookService {
    private final Constants constants;
    private final RestTemplate restTemplate;

    @Autowired
    public BookService(Constants constants, RestTemplate restTemplate) {
        this.constants = constants;
        this.restTemplate = restTemplate;
    }

    public List<BookDto> getAllBooksDto() {
        String url = constants.getBookServiceUrl() + "/book";
        Book[] bookArray = restTemplate.getForObject(url, Book[].class);
        return convertBookToBookDTO(constants.getFileServiceUrl(), bookArray);
    }

    public int getBooksCount() {
        String url = constants.getBookServiceUrl() + "/book/count";
        return restTemplate.getForObject(url, Integer.class);
    }

    public Book saveBook(String name, String lang, int year, String authors, String publisher,
                         String description, MultipartFile file, int page, int category) {
        SaveFileResponse saveFileResponse = uploadFile(file, page);

        List<String> authorsList = prepareAuthors(authors);
        String saveBookUrl = constants.getBookServiceUrl() + "/book/save";
        return restTemplate.postForObject(
                saveBookUrl,
                SaveBookRequest
                        .builder()
                        .name(name)
                        .language(lang)
                        .year(year)
                        .authors(authorsList)
                        .publisher(publisher)
                        .image(saveFileResponse.getImgPath())
                        .link(saveFileResponse.getFileId())
                        .description(description)
                        .categoryId(category)
                        .pageCount(saveFileResponse.getPageCount())
                        .build(),
                Book.class);
    }

    public boolean deleteBook(String fileId, int id, String image) {
        return deleteFile(fileId) & deleteImage(image) & deleteEntity(id);
    }

    public BookDto getBookDtoById(int id) {
        String url = constants.getBookServiceUrl() + "/book/one/update?id={id}";
        Book book = restTemplate.getForObject(url, Book.class, id);
        return convertBookToBookDTO(constants.getFileServiceUrl(), book).get(0);
    }

    public Book getBookById(int id) {
        String url = constants.getBookServiceUrl() + "/book/one/update?id={id}";
        return restTemplate.getForObject(url, Book.class, id);
    }

    public Book updateBook(SaveBookRequest book) {
        String url = constants.getBookServiceUrl() + "/book/update";
        book.setImage(book.getImage().replaceAll(constants.getFileServiceUrl(), ""));
        return restTemplate.postForObject(url, book, Book.class);
    }

    public void updateImage(MultipartFile file, int id, String oldLink) {
        String newLink = replaceImage(file, oldLink);
        Book updateBook = getBookById(id);
        updateBook(SaveBookRequest
                .builder()
                .id(id)
                .name(updateBook.getName())
                .language(updateBook.getLanguage())
                .year(updateBook.getYear())
                .authors(prepareAuthors(updateBook.getAuthor()))
                .publisher(updateBook.getPublisher().getName())
                .image(newLink)
                .link(updateBook.getFileId())
                .description(updateBook.getDescription())
                .categoryId(updateBook.getCategory().getId())
                .pageCount(updateBook.getPageCount())
                .build()
        );
    }

    private String replaceImage(MultipartFile file, String oldLink) {
        String saveFileUrl = constants.getFileServiceUrl() + "/jpg/replace";
        File tempFile = convert(file);
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(tempFile));
        map.add("link", oldLink);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        String newLink;
        try {
            newLink = restTemplate.postForObject(saveFileUrl, requestEntity, String.class);
        } catch (Exception e) {
            e.getMessage();
            throw new SaveEntityException("Cannot replace file");
        } finally {
            tempFile.delete();
        }
        return newLink;
    }

    private SaveFileResponse uploadFile(MultipartFile file, int page) {
        String saveFileUrl = constants.getFileServiceUrl() + "/pdf/upload";
        File tempFile = convert(file);

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new FileSystemResource(tempFile));
        map.add("page", page);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        try {
            return restTemplate.postForObject(saveFileUrl, requestEntity, SaveFileResponse.class);
        } catch (Exception e) {
            e.getMessage();
            throw new SaveEntityException("Cannot upload file");
        } finally {
            tempFile.delete();
        }
    }

    public static List<String> prepareAuthors(String authors) {
        return Arrays.stream(authors.split(",")).map(String::trim).collect(Collectors.toList());
    }

    private static List<String> prepareAuthors(List<Author> authors) {
        return authors.stream().map(Author::getName).collect(Collectors.toList());
    }


    private File convert(MultipartFile file) {
        try {
            File convertFile = new File(Translit.cyr2lat(file.getOriginalFilename()));
            FileOutputStream fos = new FileOutputStream(convertFile);
            fos.write(file.getBytes());
            fos.close();
            return convertFile;
        } catch (Exception e) {
            throw new SaveEntityException("Cannot convert MultipartFile to File");
        }
    }

    private boolean deleteEntity(int id) {
        try {
            String url = constants.getBookServiceUrl() + "/book/delete?id=" + id;
            return restTemplate.getForObject(url, Boolean.class);
        } catch (Exception e) {
            log.warn("Exception, while deleting book entity with id " + id);
            return false;
        }
    }

    private boolean deleteImage(String image) {
        try {
            String url = constants.getFileServiceUrl() + "/jpg/delete?link=" + image;
            return restTemplate.getForObject(url, Boolean.class);
        } catch (Exception e) {
            log.warn("Exception, while deleting image " + image);
            return false;
        }
    }

    private boolean deleteFile(String fileId) {
        try {
            String url = constants.getFileServiceUrl() + "/pdf/delete?id=" + fileId;
            return restTemplate.getForObject(url, Boolean.class);
        } catch (Exception e) {
            log.warn("Exception, while deleting book with file id = " + fileId);
            return false;
        }
    }
}
