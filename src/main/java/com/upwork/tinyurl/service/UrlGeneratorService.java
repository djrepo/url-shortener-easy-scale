package com.upwork.tinyurl.service;

import com.upwork.tinyurl.model.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlGeneratorService {

    private static final int CHAR_COUNT = 62;
    private static final char[] ARR = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9'
    };

    @Autowired
    private SharedRangeService sharedRangeService;

    @Autowired
    private Range range;

    @Value("#{range.start}")
    private long pointer;

    private synchronized void resetRange() {
        range = sharedRangeService.getUniqueRange();
        pointer = range.getStart();
    }

    public synchronized String createShortUrl(String original) {
        if (pointer > range.getEnd()) {
            resetRange();
        }
        long count = pointer;
        StringBuilder sNewUrl = new StringBuilder();
        while (count > 0) {
            sNewUrl.append(ARR[(int) (count % CHAR_COUNT)]);
            count /= CHAR_COUNT;
        }
        while (sNewUrl.length() < 7) {
            sNewUrl.append('a');
        }
        pointer++;
        return sNewUrl.reverse().toString();
    }
}
