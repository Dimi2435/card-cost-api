package com.etraveligroup.cardcostapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic paged response wrapper for REST APIs. Provides pagination metadata and content for any
 * type of data.
 *
 * <p>Author: Dimitrios Milios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated response wrapper")
public class PagedResponse<T> {

  @Schema(description = "List of items for current page")
  private List<T> content;

  @Schema(description = "Current page number (0-based)")
  private int page;

  @Schema(description = "Number of items per page")
  private int size;

  @Schema(description = "Total number of elements across all pages")
  private long totalElements;

  @Schema(description = "Total number of pages")
  private int totalPages;

  @Schema(description = "Whether this is the first page")
  private boolean first;

  @Schema(description = "Whether this is the last page")
  private boolean last;

  @Schema(description = "Number of elements in current page")
  private int numberOfElements;

  @Schema(description = "Whether the page is empty")
  private boolean empty;
}
