package uk.nhs.ctp.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Setting implements Concept {

  ONLINE("online", "Online"),
  PHONE("phone", "Phone Call"),
  FACE_TO_FACE("clinical", "Face to face");

  private final String value;
  private final String display;

  public static Setting fromCode(String code) {
    return Arrays.stream(Setting.values())
        .filter(type -> type.value.equals(code))
        .findFirst().orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public String getSystem() {
    return "setting";
  }
}
