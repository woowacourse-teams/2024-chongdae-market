package com.zzang.chongdae.offering.controller;


import com.zzang.chongdae.offering.service.OfferingService;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponse;
import com.zzang.chongdae.offering.service.dto.OfferingAllResponseItem;
import com.zzang.chongdae.offering.service.dto.OfferingFilterAllResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OfferingReadOnlyController {

    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 60;

    private final OfferingService offeringService;

    @GetMapping("/read-only/offerings")
    public ResponseEntity<OfferingAllResponse> getAllOffering(
            @RequestParam(value = "filter", defaultValue = "RECENT") String filterName,
            @RequestParam(value = "search", required = false) String searchKeyword,
            @RequestParam(value = "last-id", required = false) Long lastId,
            @RequestParam(value = "page-size", defaultValue = "10") @Min(MIN_PAGE_SIZE) @Max(MAX_PAGE_SIZE)
            Integer pageSize) {
        OfferingAllResponse response = offeringService.getAllOffering(filterName, searchKeyword, lastId, pageSize);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/read-only/offerings/filters")
    public ResponseEntity<OfferingFilterAllResponse> getAllOfferingFilter() {
        OfferingFilterAllResponse response = offeringService.getAllOfferingFilter();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/read-only/offerings/{offering-id}")
    public ResponseEntity<OfferingAllResponseItem> getOffering(
            @PathVariable(value = "offering-id") Long offeringId) {
        OfferingAllResponseItem response = offeringService.getOffering(offeringId);
        return ResponseEntity.ok(response);
    }
}
