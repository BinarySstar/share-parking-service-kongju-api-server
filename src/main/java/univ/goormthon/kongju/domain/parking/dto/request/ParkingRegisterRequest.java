package univ.goormthon.kongju.domain.parking.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class ParkingRegisterRequest {

        private String name;
        private String address;
        private Double latitude;
        private Double longitude;

        @JsonProperty("car_capacity")
        private Integer carCapacity;

        @JsonProperty("pm_capacity")
        private Integer pmCapacity;

        private String description;
        private Integer rate;
        private List<ParkingAvailabilityRequest> availabilities;

        @Setter
        private List<MultipartFile> images; // 이미지 필드 추가
}

