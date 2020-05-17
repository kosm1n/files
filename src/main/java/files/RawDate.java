package files;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

public enum RawDate {

    SAMSUNG_IMG("[1-2][0-9][0-9][0-9][0-1][0-9][0-3][0-9](_)[0-2][0-9][0-5][0-9][0-5][0-9](.jpg)", "yyyyMMdd_HHmmss"),
    SAMSUNG_VID("[1-2][0-9][0-9][0-9][0-1][0-9][0-3][0-9](_)[0-2][0-9][0-5][0-9][0-5][0-9](.mp4)", "yyyyMMdd_HHmmss"),
    SAMSUNG_OLD("(Photo taken on )[1-2][0-9][0-9][0-9](-)[0-1][0-9](-)[0-3][0-9]( )[0-2][0-9](-)[0-5][0-9](-)[0-5][0-9](.jpg)", "yyyy-MM-dd HH-mm-ss"),
    WHATSAPP_IMG("IMG-[1-2][0-9][0-9][0-9][0-1][0-9][0-3][0-9](-WA)[0-9]+(.jpg|.jpeg)", "yyyyMMdd"),
    WHATSAPP_VID("VID-[1-2][0-9][0-9][0-9][0-1][0-9][0-3][0-9](-WA)[0-9]+(.mp4)", "yyyyMMdd");

    private String pattern;
    private String format;

    RawDate(String pattern, String format) {
        this.pattern = pattern;
        this.format = format;
    }

    public String getPattern() {
        return pattern;
    }

    public String getFormat() {
        return format;
    }

    public static RawDate getDateFormatsEnum(String fileName) {
        return Arrays.stream(RawDate.values())
                .filter(value ->
                        Pattern.compile(value.getPattern()).matcher(fileName).matches())
                .findFirst().orElseThrow(() -> new RuntimeException("No date format found for the file name: " + fileName));
    }

    public static Date getDateFromFileName(String fileName) throws ParseException {
        RawDate rawDate = getDateFormatsEnum(fileName);
        switch (rawDate) {
            case SAMSUNG_IMG:
                return new SimpleDateFormat(rawDate.getFormat()).parse(fileName.replace(".jpg",""));
            case SAMSUNG_VID:
                return new SimpleDateFormat(rawDate.getFormat()).parse(fileName.replace(".mp4",""));
            case SAMSUNG_OLD:
                return new SimpleDateFormat(rawDate.getFormat())
                        .parse(
                                fileName.replaceFirst("(Photo taken on )","")
                                        .replaceFirst("(.jpg)","")
                        );
            case WHATSAPP_IMG:
                return new SimpleDateFormat(rawDate.getFormat())
                        .parse(
                                fileName.replace("IMG-","")
                                        .replaceFirst("(.jpg|.jpeg)","")
                                        .replaceFirst("(-WA)[0-9]+", "")
                        );
            case WHATSAPP_VID:
                return new SimpleDateFormat(rawDate.getFormat())
                        .parse(
                                fileName.replace("VID-","")
                                        .replace(".mp4","")
                                        .replaceFirst("(-WA)[0-9]+", "")
                );
        }
        return null;
    }

}
