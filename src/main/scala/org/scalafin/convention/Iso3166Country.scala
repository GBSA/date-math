package org.scalafin.convention

sealed abstract class Iso3166Country(val fullName: String, val threeLetterCode: String, val numberCode: Int)
// AX
case object AX extends Iso3166Country("AALAND ISLANDS", "ALA", 248)
// AF
case object AF extends Iso3166Country("AFGHANISTAN", "AFG", 4)
// AL
case object AL extends Iso3166Country("ALBANIA", "ALB", 8)
// DZ
case object DZ extends Iso3166Country("ALGERIA", "DZA", 12)
// AS
case object AS extends Iso3166Country("AMERICAN SAMOA", "ASM", 16)
// AD
case object AD extends Iso3166Country("ANDORRA", "AND", 20)
// AO
case object AO extends Iso3166Country("ANGOLA", "AGO", 24)
// AI
case object AI extends Iso3166Country("ANGUILLA", "AIA", 660)
// AQ
case object AQ extends Iso3166Country("ANTARCTICA", "ATA", 10)
// AG
case object AG extends Iso3166Country("ANTIGUA AND BARBUDA", "ATG", 28)
// AR
case object AR extends Iso3166Country("ARGENTINA", "ARG", 32)
// AM
case object AM extends Iso3166Country("ARMENIA", "ARM", 51)
// AW
case object AW extends Iso3166Country("ARUBA", "ABW", 533)
// AU
case object AU extends Iso3166Country("AUSTRALIA", "AUS", 36)
// AT
case object AT extends Iso3166Country("AUSTRIA", "AUT", 40)
// AZ
case object AZ extends Iso3166Country("AZERBAIJAN", "AZE", 31)
// BS
case object BS extends Iso3166Country("BAHAMAS", "BHS", 44)
// BH
case object BH extends Iso3166Country("BAHRAIN", "BHR", 48)
// BD
case object BD extends Iso3166Country("BANGLADESH", "BGD", 50)
// BB
case object BB extends Iso3166Country("BARBADOS", "BRB", 52)
// BY
case object BY extends Iso3166Country("BELARUS", "BLR", 112)
// BE
case object BE extends Iso3166Country("BELGIUM", "BEL", 56)
// BZ
case object BZ extends Iso3166Country("BELIZE", "BLZ", 84)
// BJ
case object BJ extends Iso3166Country("BENIN", "BEN", 204)
// BM
case object BM extends Iso3166Country("BERMUDA", "BMU", 60)
// BT
case object BT extends Iso3166Country("BHUTAN", "BTN", 64)
// BO
case object BO extends Iso3166Country("BOLIVIA", "BOL", 68)
// BA
case object BA extends Iso3166Country("BOSNIA AND HERZEGOWINA", "BIH", 70)
// BW
case object BW extends Iso3166Country("BOTSWANA", "BWA", 72)
// BV
case object BV extends Iso3166Country("BOUVET ISLAND", "BVT", 74)
// BR
case object BR extends Iso3166Country("BRAZIL", "BRA", 76)
// IO
case object IO extends Iso3166Country("BRITISH INDIAN OCEAN TERRITORY", "IOT", 86)
// BN
case object BN extends Iso3166Country("BRUNEI DARUSSALAM", "BRN", 96)
// BG
case object BG extends Iso3166Country("BULGARIA", "BGR", 100)
// BF
case object BF extends Iso3166Country("BURKINA FASO", "BFA", 854)
// BI
case object BI extends Iso3166Country("BURUNDI", "BDI", 108)
// KH
case object KH extends Iso3166Country("CAMBODIA", "KHM", 116)
// CM
case object CM extends Iso3166Country("CAMEROON", "CMR", 120)
// CA
case object CA extends Iso3166Country("CANADA", "CAN", 124)
// CV
case object CV extends Iso3166Country("CAPE VERDE", "CPV", 132)
// KY
case object KY extends Iso3166Country("CAYMAN ISLANDS", "CYM", 136)
// CF
case object CF extends Iso3166Country("CENTRAL AFRICAN REPUBLIC", "CAF", 140)
// TD
case object TD extends Iso3166Country("CHAD", "TCD", 148)
// CL
case object CL extends Iso3166Country("CHILE", "CHL", 152)
// CN
case object CN extends Iso3166Country("CHINA", "CHN", 156)
// CX
case object CX extends Iso3166Country("CHRISTMAS ISLAND", "CXR", 162)
// CC
case object CC extends Iso3166Country("COCOS  extends Iso3166CountryKEELING) ISLANDS", "CCK", 166)
// CO
case object CO extends Iso3166Country("COLOMBIA", "COL", 170)
// KM
case object KM extends Iso3166Country("COMOROS", "COM", 174)
// CD
case object CD extends Iso3166Country("CONGO, Democratic Republic of  extends Iso3166Countrywas Zaire)", "COD", 180)
// CG
case object CG extends Iso3166Country("CONGO, Republic of", "COG", 178)
// CK
case object CK extends Iso3166Country("COOK ISLANDS", "COK", 184)
// CR
case object CR extends Iso3166Country("COSTA RICA", "CRI", 188)
// CI
case object CI extends Iso3166Country("COTE D'IVOIRE", "CIV", 384)
// HR
case object HR extends Iso3166Country("CROATIA  extends Iso3166Countrylocal name: Hrvatska)", "HRV", 191)
// CU
case object CU extends Iso3166Country("CUBA", "CUB", 192)
// CY
case object CY extends Iso3166Country("CYPRUS", "CYP", 196)
// CZ
case object CZ extends Iso3166Country("CZECH REPUBLIC", "CZE", 203)
// DK
case object DK extends Iso3166Country("DENMARK", "DNK", 208)
// DJ
case object DJ extends Iso3166Country("DJIBOUTI", "DJI", 262)
// DM
case object DM extends Iso3166Country("DOMINICA", "DMA", 212)
// DO
case object DO extends Iso3166Country("DOMINICAN REPUBLIC", "DOM", 214)
// EC
case object EC extends Iso3166Country("ECUADOR", "ECU", 218)
// EG
case object EG extends Iso3166Country("EGYPT", "EGY", 818)
// SV
case object SV extends Iso3166Country("EL SALVADOR", "SLV", 222)
// GQ
case object GQ extends Iso3166Country("EQUATORIAL GUINEA", "GNQ", 226)
// ER
case object ER extends Iso3166Country("ERITREA", "ERI", 232)
// EE
case object EE extends Iso3166Country("ESTONIA", "EST", 233)
// ET
case object ET extends Iso3166Country("ETHIOPIA", "ETH", 231)
// FK
case object FK extends Iso3166Country("FALKLAND ISLANDS  extends Iso3166CountryMALVINAS)", "FLK", 238)
// FO
case object FO extends Iso3166Country("FAROE ISLANDS", "FRO", 234)
// FJ
case object FJ extends Iso3166Country("FIJI", "FJI", 242)
// FI
case object FI extends Iso3166Country("FINLAND", "FIN", 246)
// FR
case object FR extends Iso3166Country("FRANCE", "FRA", 250)
// GF
case object GF extends Iso3166Country("FRENCH GUIANA", "GUF", 254)
// PF
case object PF extends Iso3166Country("FRENCH POLYNESIA", "PYF", 258)
// TF
case object TF extends Iso3166Country("FRENCH SOUTHERN TERRITORIES", "ATF", 260)
// GA
case object GA extends Iso3166Country("GABON", "GAB", 266)
// GM
case object GM extends Iso3166Country("GAMBIA", "GMB", 270)
// GE
case object GE extends Iso3166Country("GEORGIA", "GEO", 268)
// DE
case object DE extends Iso3166Country("GERMANY", "DEU", 276)
// GH
case object GH extends Iso3166Country("GHANA", "GHA", 288)
// GI
case object GI extends Iso3166Country("GIBRALTAR", "GIB", 292)
// GR
case object GR extends Iso3166Country("GREECE", "GRC", 300)
// GL
case object GL extends Iso3166Country("GREENLAND", "GRL", 304)
// GD
case object GD extends Iso3166Country("GRENADA", "GRD", 308)
// GP
case object GP extends Iso3166Country("GUADELOUPE", "GLP", 312)
// GU
case object GU extends Iso3166Country("GUAM", "GUM", 316)
// GT
case object GT extends Iso3166Country("GUATEMALA", "GTM", 320)
// GN
case object GN extends Iso3166Country("GUINEA", "GIN", 324)
// GW
case object GW extends Iso3166Country("GUINEA-BISSAU", "GNB", 624)
// GY
case object GY extends Iso3166Country("GUYANA", "GUY", 328)
// HT
case object HT extends Iso3166Country("HAITI", "HTI", 332)
// HM
case object HM extends Iso3166Country("HEARD AND MC DONALD ISLANDS", "HMD", 334)
// HN
case object HN extends Iso3166Country("HONDURAS", "HND", 340)
// HK
case object HK extends Iso3166Country("HONG KONG", "HKG", 344)
// HU
case object HU extends Iso3166Country("HUNGARY", "HUN", 348)
// IS
case object IS extends Iso3166Country("ICELAND", "ISL", 352)
// IN
case object IN extends Iso3166Country("INDIA", "IND", 356)
// ID
case object ID extends Iso3166Country("INDONESIA", "IDN", 360)
// IR
case object IR extends Iso3166Country("IRAN  extends Iso3166CountryISLAMIC REPUBLIC OF)", "IRN", 364)
// IQ
case object IQ extends Iso3166Country("IRAQ", "IRQ", 368)
// IE
case object IE extends Iso3166Country("IRELAND", "IRL", 372)
// IL
case object IL extends Iso3166Country("ISRAEL", "ISR", 376)
// IT
case object IT extends Iso3166Country("ITALY", "ITA", 380)
// JM
case object JM extends Iso3166Country("JAMAICA", "JAM", 388)
// JP
case object JP extends Iso3166Country("JAPAN", "JPN", 392)
// JO
case object JO extends Iso3166Country("JORDAN", "JOR", 400)
// KZ
case object KZ extends Iso3166Country("KAZAKHSTAN", "KAZ", 398)
// KE
case object KE extends Iso3166Country("KENYA", "KEN", 404)
// KI
case object KI extends Iso3166Country("KIRIBATI", "KIR", 296)
// KP
case object KP extends Iso3166Country("KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF", "PRK", 408)
// KR
case object KR extends Iso3166Country("KOREA, REPUBLIC OF", "KOR", 410)
// KW
case object KW extends Iso3166Country("KUWAIT", "KWT", 414)
// KG
case object KG extends Iso3166Country("KYRGYZSTAN", "KGZ", 417)
// LA
case object LA extends Iso3166Country("LAO PEOPLE'S DEMOCRATIC REPUBLIC", "LAO", 418)
// LV
case object LV extends Iso3166Country("LATVIA", "LVA", 428)
// LB
case object LB extends Iso3166Country("LEBANON", "LBN", 422)
// LS
case object LS extends Iso3166Country("LESOTHO", "LSO", 426)
// LR
case object LR extends Iso3166Country("LIBERIA", "LBR", 430)
// LY
case object LY extends Iso3166Country("LIBYAN ARAB JAMAHIRIYA", "LBY", 434)
// LI
case object LI extends Iso3166Country("LIECHTENSTEIN", "LIE", 438)
// LT
case object LT extends Iso3166Country("LITHUANIA", "LTU", 440)
// LU
case object LU extends Iso3166Country("LUXEMBOURG", "LUX", 442)
// MO
case object MO extends Iso3166Country("MACAU", "MAC", 446)
// MK
case object MK extends Iso3166Country("MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", "MKD", 807)
// MG
case object MG extends Iso3166Country("MADAGASCAR", "MDG", 450)
// MW
case object MW extends Iso3166Country("MALAWI", "MWI", 454)
// MY
case object MY extends Iso3166Country("MALAYSIA", "MYS", 458)
// MV
case object MV extends Iso3166Country("MALDIVES", "MDV", 462)
// ML
case object ML extends Iso3166Country("MALI", "MLI", 466)
// MT
case object MT extends Iso3166Country("MALTA", "MLT", 470)
// MH
case object MH extends Iso3166Country("MARSHALL ISLANDS", "MHL", 584)
// MQ
case object MQ extends Iso3166Country("MARTINIQUE", "MTQ", 474)
// MR
case object MR extends Iso3166Country("MAURITANIA", "MRT", 478)
// MU
case object MU extends Iso3166Country("MAURITIUS", "MUS", 480)
// YT
case object YT extends Iso3166Country("MAYOTTE", "MYT", 175)
// MX
case object MX extends Iso3166Country("MEXICO", "MEX", 484)
// FM
case object FM extends Iso3166Country("MICRONESIA, FEDERATED STATES OF", "FSM", 583)
// MD
case object MD extends Iso3166Country("MOLDOVA, REPUBLIC OF", "MDA", 498)
// MC
case object MC extends Iso3166Country("MONACO", "MCO", 492)
// MN
case object MN extends Iso3166Country("MONGOLIA", "MNG", 496)
// MS
case object MS extends Iso3166Country("MONTSERRAT", "MSR", 500)
// MA
case object MA extends Iso3166Country("MOROCCO", "MAR", 504)
// MZ
case object MZ extends Iso3166Country("MOZAMBIQUE", "MOZ", 508)
// MM
case object MM extends Iso3166Country("MYANMAR", "MMR", 104)
// NA
case object NA extends Iso3166Country("NAMIBIA", "NAM", 516)
// NR
case object NR extends Iso3166Country("NAURU", "NRU", 520)
// NP
case object NP extends Iso3166Country("NEPAL", "NPL", 524)
// NL
case object NL extends Iso3166Country("NETHERLANDS", "NLD", 528)
// AN
case object AN extends Iso3166Country("NETHERLANDS ANTILLES", "ANT", 530)
// NC
case object NC extends Iso3166Country("NEW CALEDONIA", "NCL", 540)
// NZ
case object NZ extends Iso3166Country("NEW ZEALAND", "NZL", 554)
// NI
case object NI extends Iso3166Country("NICARAGUA", "NIC", 558)
// NE
case object NE extends Iso3166Country("NIGER", "NER", 562)
// NG
case object NG extends Iso3166Country("NIGERIA", "NGA", 566)
// NU
case object NU extends Iso3166Country("NIUE", "NIU", 570)
// NF
case object NF extends Iso3166Country("NORFOLK ISLAND", "NFK", 574)
// MP
case object MP extends Iso3166Country("NORTHERN MARIANA ISLANDS", "MNP", 580)
// NO
case object NO extends Iso3166Country("NORWAY", "NOR", 578)
// OM
case object OM extends Iso3166Country("OMAN", "OMN", 512)
// PK
case object PK extends Iso3166Country("PAKISTAN", "PAK", 586)
// PW
case object PW extends Iso3166Country("PALAU", "PLW", 585)
// PS
case object PS extends Iso3166Country("PALESTINIAN TERRITORY, Occupied", "PSE", 275)
// PA
case object PA extends Iso3166Country("PANAMA", "PAN", 591)
// PG
case object PG extends Iso3166Country("PAPUA NEW GUINEA", "PNG", 598)
// PY
case object PY extends Iso3166Country("PARAGUAY", "PRY", 600)
// PE
case object PE extends Iso3166Country("PERU", "PER", 604)
// PH
case object PH extends Iso3166Country("PHILIPPINES", "PHL", 608)
// PN
case object PN extends Iso3166Country("PITCAIRN", "PCN", 612)
// PL
case object PL extends Iso3166Country("POLAND", "POL", 616)
// PT
case object PT extends Iso3166Country("PORTUGAL", "PRT", 620)
// PR
case object PR extends Iso3166Country("PUERTO RICO", "PRI", 630)
// QA
case object QA extends Iso3166Country("QATAR", "QAT", 634)
// RE
case object RE extends Iso3166Country("REUNION", "REU", 638)
// RO
case object RO extends Iso3166Country("ROMANIA", "ROU", 642)
// RU
case object RU extends Iso3166Country("RUSSIAN FEDERATION", "RUS", 643)
// RW
case object RW extends Iso3166Country("RWANDA", "RWA", 646)
// SH
case object SH extends Iso3166Country("SAINT HELENA", "SHN", 654)
// KN
case object KN extends Iso3166Country("SAINT KITTS AND NEVIS", "KNA", 659)
// LC
case object LC extends Iso3166Country("SAINT LUCIA", "LCA", 662)
// PM
case object PM extends Iso3166Country("SAINT PIERRE AND MIQUELON", "SPM", 666)
// VC
case object VC extends Iso3166Country("SAINT VINCENT AND THE GRENADINES", "VCT", 670)
// WS
case object WS extends Iso3166Country("SAMOA", "WSM", 882)
// SM
case object SM extends Iso3166Country("SAN MARINO", "SMR", 674)
// ST
case object ST extends Iso3166Country("SAO TOME AND PRINCIPE", "STP", 678)
// SA
case object SA extends Iso3166Country("SAUDI ARABIA", "SAU", 682)
// SN
case object SN extends Iso3166Country("SENEGAL", "SEN", 686)
// CS
case object CS extends Iso3166Country("SERBIA AND MONTENEGRO", "SCG", 891)
// SC
case object SC extends Iso3166Country("SEYCHELLES", "SYC", 690)
// SL
case object SL extends Iso3166Country("SIERRA LEONE", "SLE", 694)
// SG
case object SG extends Iso3166Country("SINGAPORE", "SGP", 702)
// SK
case object SK extends Iso3166Country("SLOVAKIA", "SVK", 703)
// SI
case object SI extends Iso3166Country("SLOVENIA", "SVN", 705)
// SB
case object SB extends Iso3166Country("SOLOMON ISLANDS", "SLB", 90)
// SO
case object SO extends Iso3166Country("SOMALIA", "SOM", 706)
// ZA
case object ZA extends Iso3166Country("SOUTH AFRICA", "ZAF", 710)
// GS
case object GS extends Iso3166Country("SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS", "SGS", 239)
// ES
case object ES extends Iso3166Country("SPAIN", "ESP", 724)
// LK
case object LK extends Iso3166Country("SRI LANKA", "LKA", 144)
// SD
case object SD extends Iso3166Country("SUDAN", "SDN", 736)
// SR
case object SR extends Iso3166Country("SURINAME", "SUR", 740)
// SJ
case object SJ extends Iso3166Country("SVALBARD AND JAN MAYEN ISLANDS", "SJM", 744)
// SZ
case object SZ extends Iso3166Country("SWAZILAND", "SWZ", 748)
// SE
case object SE extends Iso3166Country("SWEDEN", "SWE", 752)
// CH
case object CH extends Iso3166Country("SWITZERLAND", "CHE", 756)
// SY
case object SY extends Iso3166Country("SYRIAN ARAB REPUBLIC", "SYR", 760)
// TW
case object TW extends Iso3166Country("TAIWAN", "TWN", 158)
// TJ
case object TJ extends Iso3166Country("TAJIKISTAN", "TJK", 762)
// TZ
case object TZ extends Iso3166Country("TANZANIA, UNITED REPUBLIC OF", "TZA", 834)
// TH
case object TH extends Iso3166Country("THAILAND", "THA", 764)
// TL
case object TL extends Iso3166Country("TIMOR-LESTE", "TLS", 626)
// TG
case object TG extends Iso3166Country("TOGO", "TGO", 768)
// TK
case object TK extends Iso3166Country("TOKELAU", "TKL", 772)
// TO
case object TO extends Iso3166Country("TONGA", "TON", 776)
// TT
case object TT extends Iso3166Country("TRINIDAD AND TOBAGO", "TTO", 780)
// TN
case object TN extends Iso3166Country("TUNISIA", "TUN", 788)
// TR
case object TR extends Iso3166Country("TURKEY", "TUR", 792)
// TM
case object TM extends Iso3166Country("TURKMENISTAN", "TKM", 795)
// TC
case object TC extends Iso3166Country("TURKS AND CAICOS ISLANDS", "TCA", 796)
// TV
case object TV extends Iso3166Country("TUVALU", "TUV", 798)
// UG
case object UG extends Iso3166Country("UGANDA", "UGA", 800)
// UA
case object UA extends Iso3166Country("UKRAINE", "UKR", 804)
// AE
case object AE extends Iso3166Country("UNITED ARAB EMIRATES", "ARE", 784)
// GB
case object GB extends Iso3166Country("UNITED KINGDOM", "GBR", 826)
// US
case object US extends Iso3166Country("UNITED STATES", "USA", 840)
// UM
case object UM extends Iso3166Country("UNITED STATES MINOR OUTLYING ISLANDS", "UMI", 581)
// UY
case object UY extends Iso3166Country("URUGUAY", "URY", 858)
// UZ
case object UZ extends Iso3166Country("UZBEKISTAN", "UZB", 860)
// VU
case object VU extends Iso3166Country("VANUATU", "VUT", 548)
// VA
case object VA extends Iso3166Country("VATICAN CITY STATE  extends Iso3166CountryHOLY SEE)", "VAT", 336)
// VE
case object VE extends Iso3166Country("VENEZUELA", "VEN", 862)
// VN
case object VN extends Iso3166Country("VIET NAM", "VNM", 704)
// VG
case object VG extends Iso3166Country("VIRGIN ISLANDS  extends Iso3166CountryBRITISH)", "VGB", 92)
// VI
case object VI extends Iso3166Country("VIRGIN ISLANDS  extends Iso3166CountryU.S.)", "VIR", 850)
// WF
case object WF extends Iso3166Country("WALLIS AND FUTUNA ISLANDS", "WLF", 876)
// EH
case object EH extends Iso3166Country("WESTERN SAHARA", "ESH", 732)
// YE
case object YE extends Iso3166Country("YEMEN", "YEM", 887)
// ZM
case object ZM extends Iso3166Country("ZAMBIA", "ZMB", 894)
// ZW
case object ZW extends Iso3166Country("ZIMBABWE", "ZWE", 716)

// Not an ISO Country, but included because it often has to be thought of and dealt with as a country
case object EU extends Iso3166Country("EUROPEAN UNION", "", -1)
