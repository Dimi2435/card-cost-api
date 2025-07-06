// package com.etraveligroup.cardcostapi.dto;

// import static org.junit.jupiter.api.Assertions.*;
// import org.junit.jupiter.api.Test;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.List;

// /**
//  * Unit tests for PagedResponse class.
//  * This class tests the functionality of the paginated response wrapper.
//  *
//  * Author: Dimitrios Milios
//  */
// class PagedResponseTest {

//     @Test
//     void testPagedResponse_ValidData() {
//         List<String> content = Arrays.asList("item1", "item2");
//         PagedResponse<String> response = new PagedResponse<>(content, 0, 2, 5L, 3, true, false,
// 2);

//         assertEquals(2, response.getContent().size(), "Content size should match");
//         assertEquals(0, response.getPage(), "Page number should match");
//         assertEquals(2, response.getSize(), "Size should match");
//         assertEquals(5L, response.getTotalElements(), "Total elements should match");
//         assertEquals(3, response.getTotalPages(), "Total pages should match");
//         assertTrue(response.isFirst(), "Should be the first page");
//         assertFalse(response.isLast(), "Should not be the last page");
//         assertEquals(2, response.getNumberOfElements(), "Number of elements should match");
//         assertFalse(response.isEmpty(), "Response should not be empty");
//     }

//     @Test
//     void testPagedResponse_EmptyContent() {
//         PagedResponse<String> response = new PagedResponse<>(Collections.emptyList(), 0, 0, 0L,
// 0, true, true, 0);

//         assertTrue(response.getContent().isEmpty(), "Content should be empty");
//         assertEquals(0, response.getTotalElements(), "Total elements should be zero");
//         assertTrue(response.isEmpty(), "Response should be empty");
//     }
// }
