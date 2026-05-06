package com.streetarts.backend;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Coordinates geocode(String address) {
        List<String> variants = List.of(
                address,
                normalizeUkrainianAddress(address),
                transliterateAddress(normalizeUkrainianAddress(address)),
                transliterateAddress(address)
        );

        for (String variant : variants) {
            Coordinates coordinates = geocodeSingle(variant);

            if (coordinates != null) {
                return coordinates;
            }
        }

        return null;
    }

    private String normalizeUkrainianAddress(String address) {
        return address
                .replace("вул.", "вулиця")
                .replace("вул ", "вулиця ")
                .replace("ул.", "вулиця")
                .replace("ул ", "вулиця ")
                .replace("Київ", "Kyiv")
                .replace("Суми", "Sumy")
                .replace("Хрещатик", "Khreshchatyk")
                .replace("Соборна", "Soborna")
                .replace("вулиця", "Street")
                .trim();
    }

    private String transliterateAddress(String address) {
        return address
                .replace("А", "A").replace("а", "a")
                .replace("Б", "B").replace("б", "b")
                .replace("В", "V").replace("в", "v")
                .replace("Г", "H").replace("г", "h")
                .replace("Ґ", "G").replace("ґ", "g")
                .replace("Д", "D").replace("д", "d")
                .replace("Е", "E").replace("е", "e")
                .replace("Є", "Ye").replace("є", "ie")
                .replace("Ж", "Zh").replace("ж", "zh")
                .replace("З", "Z").replace("з", "z")
                .replace("И", "Y").replace("и", "y")
                .replace("І", "I").replace("і", "i")
                .replace("Ї", "Yi").replace("ї", "i")
                .replace("Й", "Y").replace("й", "i")
                .replace("К", "K").replace("к", "k")
                .replace("Л", "L").replace("л", "l")
                .replace("М", "M").replace("м", "m")
                .replace("Н", "N").replace("н", "n")
                .replace("О", "O").replace("о", "o")
                .replace("П", "P").replace("п", "p")
                .replace("Р", "R").replace("р", "r")
                .replace("С", "S").replace("с", "s")
                .replace("Т", "T").replace("т", "t")
                .replace("У", "U").replace("у", "u")
                .replace("Ф", "F").replace("ф", "f")
                .replace("Х", "Kh").replace("х", "kh")
                .replace("Ц", "Ts").replace("ц", "ts")
                .replace("Ч", "Ch").replace("ч", "ch")
                .replace("Ш", "Sh").replace("ш", "sh")
                .replace("Щ", "Shch").replace("щ", "shch")
                .replace("Ю", "Yu").replace("ю", "iu")
                .replace("Я", "Ya").replace("я", "ia")
                .replace("ь", "")
                .replace("’", "")
                .replace("'", "");
    }

//    public Coordinates geocode(String address) {
//        List<String> variants = List.of(
//                address,
//                address + ", Україна",
//                address.replace("вул.", "вулиця"),
//                address.replace("ул.", "вулиця"),
//                "Ukraine, " + address
//        );
//
//        for (String variant : variants) {
//            Coordinates coordinates = geocodeSingle(variant);
//
//            if (coordinates != null) {
//                return coordinates;
//            }
//        }
//
//        return null;
//    }

    private Coordinates geocodeSingle(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);

            String url = "https://nominatim.openstreetmap.org/search"
                    + "?q=" + encodedAddress
                    + "&format=json"
                    + "&limit=1"
                    + "&countrycodes=ua"
                    + "&accept-language=uk";

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "StreetArtLive/1.0 student-project");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<NominatimResult[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    NominatimResult[].class
            );

            NominatimResult[] results = response.getBody();

            System.out.println("GEOCODING TRY: " + address);

            if (results == null || results.length == 0) {
                System.out.println("GEOCODING RESPONSE: empty");
                return null;
            }

            double latitude = Double.parseDouble(results[0].lat);
            double longitude = Double.parseDouble(results[0].lon);

            System.out.println("GEOCODING LAT: " + latitude);
            System.out.println("GEOCODING LON: " + longitude);

            return new Coordinates(latitude, longitude);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public Coordinates geocode(String address) {
//        try {
//            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
//
//            String url = "https://nominatim.openstreetmap.org/search"
//                    + "?q=" + encodedAddress
//                    + "&format=json"
//                    + "&limit=1";
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("User-Agent", "StreetArtLive/1.0");
//
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<NominatimResult[]> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    entity,
//                    NominatimResult[].class
//            );
//
//            NominatimResult[] results = response.getBody();
//
//            System.out.println("GEOCODING ADDRESS: " + address);
//
//            if (results == null || results.length == 0) {
//                System.out.println("GEOCODING RESPONSE: empty");
//                return null;
//            }
//
//            System.out.println("GEOCODING LAT: " + results[0].lat);
//            System.out.println("GEOCODING LON: " + results[0].lon);
//
//            double latitude = Double.parseDouble(results[0].lat);
//            double longitude = Double.parseDouble(results[0].lon);
//
//            return new Coordinates(latitude, longitude);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static class NominatimResult {
        public String lat;
        public String lon;
    }

    public static class Coordinates {
        private final double latitude;
        private final double longitude;

        public Coordinates(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
//public class GeocodingService {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public Coordinates geocode(String address) {
//        try {
//            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
//
//            String url = "https://nominatim.openstreetmap.org/search" +
//                    "?format=json" +
//                    "&limit=1" +
//                    "&q=" + encodedAddress;
//
////            String url = "https://nominatim.openstreetmap.org/search" +
////                    "?format=json" +
////                    "&limit=1" +
////                    "&countrycodes=ua" +
////                    "&accept-language=uk" +
////                    "&q=" + encodedAddress;
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("User-Agent", "DiplomaProject/1.0 (student project)");
//
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<NominatimResult[]> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    entity,
//                    NominatimResult[].class
//            );
//            System.out.println("GEOCODING ADDRESS: " + address);
//            System.out.println("GEOCODING RESPONSE: " + response.getBody());
//            NominatimResult[] results = response.getBody();
//
//            if (results != null && results.length > 0) {
//                double lat = Double.parseDouble(results[0].getLat());
//                double lon = Double.parseDouble(results[0].getLon());
//                return new Coordinates(lat, lon);
//            }
//
//            return null;
//
//        } catch (Exception e) {
//            System.out.println("Geocoding error: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public static class Coordinates {
//        private final double latitude;
//        private final double longitude;
//
//        public Coordinates(double latitude, double longitude) {
//            this.latitude = latitude;
//            this.longitude = longitude;
//        }
//
//        public double getLatitude() {
//            return latitude;
//        }
//
//        public double getLongitude() {
//            return longitude;
//        }
//    }
//
//    public static class NominatimResult {
//        private String lat;
//        private String lon;
//
//        public String getLat() {
//            return lat;
//        }
//
//        public void setLat(String lat) {
//            this.lat = lat;
//        }
//
//        public String getLon() {
//            return lon;
//        }
//
//        public void setLon(String lon) {
//            this.lon = lon;
//        }
//    }
//}