package uk.nhs.ctp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.language.bm.Lang;

@Getter
@RequiredArgsConstructor
public enum Language implements Concept {

  Q1("Braille"),
  Q2("American Sign Language"),
  Q3("Australian Sign Language"),
  Q4("British Sign Language"),
  Q5("Makaton"),
  AA("Afar"),
  AB("Abkhazian"),
  AF("Afrikaans"),
  AK("Akan"),
  SQ("Albanian"),
  AM("Amharic"),
  AR("Arabic"),
  AN("Aragonese"),
  HY("Armenian"),
  AS("Assamese"),
  AV("Avaric"),
  AE("Avestan"),
  AY("Aymara"),
  AZ("Azerbaijani"),
  BA("Bashkir"),
  BM("Bambara"),
  EU("Basque"),
  BE("Belarusian"),
  BN("Bengali"),
  BH("Bihari languages"),
  BI("Bislama"),
  BO("Tibetan"),
  BS("Bosnian"),
  BR("Breton"),
  BG("Bulgarian"),
  MY("Burmese"),
  CA("Catalan; Valencian"),
  CS("Czech"),
  CH("Chamorro"),
  CE("Chechen"),
  ZH("Chinese"),
  CU("Church Slavic; Old Slavonic; Church Slavonic; Old Bulgarian; Old Church Slavonic"),
  CV("Chuvash"),
  KW("Cornish"),
  CO("Corsican"),
  CR("Cree"),
  CY("Welsh"),
  DA("Danish"),
  DE("German"),
  DV("Divehi; Dhivehi; Maldivian"),
  NL("Dutch; Flemish"),
  DZ("Dzongkha"),
  EL("Greek, Modern (1453-)"),
  EN("English"),
  EO("Esperanto"),
  ET("Estonian"),
  EE("Ewe"),
  FO("Faroese"),
  FA("Persian"),
  FJ("Fijian"),
  FI("Finnish"),
  FR("French"),
  FY("Western Frisian"),
  FF("Fulah"),
  KA("Georgian"),
  GD("Gaelic; Scottish Gaelic"),
  GA("Irish"),
  GL("Galician"),
  GV("Manx"),
  GN("Guarani"),
  GU("Gujarati"),
  HT("Haitian; Haitian Creole"),
  HA("Hausa"),
  HE("Hebrew"),
  HZ("Herero"),
  HI("Hindi"),
  HO("Hiri Motu"),
  HR("Croatian"),
  HU("Hungarian"),
  IG("Igbo"),
  IS("Icelandic"),
  IO("Ido"),
  II("Sichuan Yi; Nuosu"),
  IU("Inuktitut"),
  IE("Interlingue; Occidental"),
  IA("Interlingua (International Auxiliary Language Association)"),
  ID("Indonesian"),
  IK("Inupiaq"),
  IT("Italian"),
  JV("Javanese"),
  JA("Japanese"),
  KL("Kalaallisut; Greenlandic"),
  KN("Kannada"),
  KS("Kashmiri"),
  KR("Kanuri"),
  KK("Kazakh"),
  KM("Central Khmer"),
  KI("Kikuyu; Gikuyu"),
  RW("Kinyarwanda"),
  KY("Kirghiz; Kyrgyz"),
  KV("Komi"),
  KG("Kongo"),
  KO("Korean"),
  KJ("Kuanyama; Kwanyama"),
  KU("Kurdish"),
  LO("Lao"),
  LA("Latin"),
  LV("Latvian"),
  LI("Limburgan; Limburger; Limburgish"),
  LN("Lingala"),
  LT("Lithuanian"),
  LB("Luxembourgish; Letzeburgesch"),
  LU("Luba-Katanga"),
  LG("Ganda"),
  MK("Macedonian"),
  MH("Marshallese"),
  ML("Malayalam"),
  MI("Maori"),
  MR("Marathi"),
  MS("Malay"),
  MG("Malagasy"),
  MT("Maltese"),
  MN("Mongolian"),
  NA("Nauru"),
  NV("Navajo; Navaho"),
  NR("Ndebele, South; South Ndebele"),
  ND("Ndebele, North; North Ndebele"),
  NG("Ndonga"),
  NE("Nepali"),
  NN("Norwegian Nynorsk; Nynorsk, Norwegian"),
  NB("Bokmal, Norwegian; Norwegian Bokmal"),
  NO("Norwegian"),
  NY("Chichewa; Chewa; Nyanja"),
  OC("Occitan (post1500)"),
  OJ("Ojibwa"),
  OR("Oriya"),
  OM("Oromo"),
  OS("Ossetian; Ossetic"),
  PA("Panjabi; Punjabi"),
  PI("Pali"),
  PL("Polish"),
  PT("Portuguese"),
  PS("Pushto; Pashto"),
  QU("Quechua"),
  RM("Romansh"),
  RO("Romanian; Moldavian; Moldovan"),
  RN("Rundi"),
  RU("Russian"),
  SG("Sango"),
  SA("Sanskrit"),
  SR("Serbian"),
  SI("Sinhala; Sinhalese"),
  SK("Slovak"),
  SL("Slovenian"),
  SE("Northern Sami"),
  SM("Samoan"),
  SN("Shona"),
  SD("Sindhi"),
  SO("Somali"),
  ST("Sotho, Southern"),
  ES("Spanish; Castilian"),
  SC("Sardinian"),
  SS("Swati"),
  SU("Sundanese"),
  SW("Swahili"),
  SV("Swedish"),
  TY("Tahitian"),
  TA("Tamil"),
  TT("Tatar"),
  TE("Telugu"),
  TG("Tajik"),
  TL("Tagalog"),
  TH("Thai"),
  TI("Tigrinya"),
  TO("Tonga (Tonga Islands)"),
  TN("Tswana"),
  TS("Tsonga"),
  TK("Turkmen"),
  TR("Turkish"),
  TW("Twi"),
  UG("Uighur; Uyghur"),
  UK("Ukrainian"),
  UR("Urdu"),
  UZ("Uzbek"),
  VE("Venda"),
  VI("Vietnamese"),
  VO("Volapuk"),
  WA("Walloon"),
  WO("Wolof"),
  XH("Xhosa"),
  YI("Yiddish"),
  YO("Yoruba"),
  ZA("Zhuang; Chuang"),
  ZU("Zulu");

  private final String system = "https://fhir.hl7.org.uk/STU3/CodeSystem/CareConnect-HumanLanguage-1";
  private final String value = name().toLowerCase();
  private final String display;

  public static Language fromCode(String code) {
    return Language.valueOf(code.toUpperCase());
  }
}
