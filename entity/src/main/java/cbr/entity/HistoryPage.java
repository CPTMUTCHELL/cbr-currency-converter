package cbr.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HistoryPage {
    @ApiModelProperty(notes = "List of converts")
    private final List<PresentationDto> dto;
    @ApiModelProperty(notes = "Max amount of pages")
    private final int totalPages;
    @ApiModelProperty(notes = "Max amount of converts")
    private final long totalElements;

}
