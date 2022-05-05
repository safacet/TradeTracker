package com.safacet.tradetracker.utils

const val HOME_STOCK_VIEW_TYPE = 9812
const val HOME_HISTORY_VIEW_TYPE = 5478
const val STOCK_TAB_POSITION = 0

const val dateFormat = "dd MMM yyyy"



enum class InputTypes {
    INPUT_CORRECT,
    OVER_TO_AMOUNT_ERROR,
    BLANKED_FIELD,
    NUMBER_FORMAT_ERROR
}

val SUPPORTED_SYMBOLS = mapOf<String, String>(
    "AED : United Arab Emirates Dirham" to "AED",
    "AFN : Afghan Afghani" to "AFN",
    "ALL : Albanian Lek" to "ALL",
    "AMD : Armenian Dram" to "AMD",
    "ANG : Netherlands Antillean Guilder" to "ANG",
    "AOA : Angolan Kwanza" to "AOA",
    "ARS : Argentine Peso" to "ARS",
    "AUD : Australian Dollar" to "AUD",
    "AWG : Aruban Florin" to "AWG",
    "AZN : Azerbaijani Manat" to "AZN",
    "BAM : Bosnia-Herzegovina Convertible Mark" to "BAM",
    "BBD : Barbadian Dollar" to "BBD",
    "BDT : Bangladeshi Taka" to "BDT",
    "BGN : Bulgarian Lev" to "BGN",
    "BHD : Bahraini Dinar" to "BHD",
    "BIF : Burundian Franc" to "BIF",
    "BMD : Bermudan Dollar" to "BMD",
    "BND : Brunei Dollar" to "BND",
    "BOB : Bolivian Boliviano" to "BOB",
    "BRL : Brazilian Real" to "BRL",
    "BSD : Bahamian Dollar" to "BSD",
    "BTC : Bitcoin" to "BTC",
    "BTN : Bhutanese Ngultrum" to "BTN",
    "BWP : Botswanan Pula" to "BWP",
    "BYN : New Belarusian Ruble" to "BYN",
    "BYR : Belarusian Ruble" to "BYR",
    "BZD : Belize Dollar" to "BZD",
    "CAD : Canadian Dollar" to "CAD",
    "CDF : Congolese Franc" to "CDF",
    "CHF : Swiss Franc" to "CHF",
    "CLF : Chilean Unit of Account (UF)" to "CLF",
    "CLP : Chilean Peso" to "CLP",
    "CNY : Chinese Yuan" to "CNY",
    "COP : Colombian Peso" to "COP",
    "CRC : Costa Rican Colón" to "CRC",
    "CUC : Cuban Convertible Peso" to "CUC",
    "CUP : Cuban Peso" to "CUP",
    "CVE : Cape Verdean Escudo" to "CVE",
    "CZK : Czech Republic Koruna" to "CZK",
    "DJF : Djiboutian Franc" to "DJF",
    "DKK : Danish Krone" to "DKK",
    "DOP : Dominican Peso" to "DOP",
    "DZD : Algerian Dinar" to "DZD",
    "EGP : Egyptian Pound" to "EGP",
    "ERN : Eritrean Nakfa" to "ERN",
    "ETB : Ethiopian Birr" to "ETB",
    "EUR : Euro" to "EUR",
    "FJD : Fijian Dollar" to "FJD",
    "FKP : Falkland Islands Pound" to "FKP",
    "GBP : British Pound Sterling" to "GBP",
    "GEL : Georgian Lari" to "GEL",
    "GGP : Guernsey Pound" to "GGP",
    "GHS : Ghanaian Cedi" to "GHS",
    "GIP : Gibraltar Pound" to "GIP",
    "GMD : Gambian Dalasi" to "GMD",
    "GNF : Guinean Franc" to "GNF",
    "GTQ : Guatemalan Quetzal" to "GTQ",
    "GYD : Guyanaese Dollar" to "GYD",
    "HKD : Hong Kong Dollar" to "HKD",
    "HNL : Honduran Lempira" to "HNL",
    "HRK : Croatian Kuna" to "HRK",
    "HTG : Haitian Gourde" to "HTG",
    "HUF : Hungarian Forint" to "HUF",
    "IDR : Indonesian Rupiah" to "IDR",
    "ILS : Israeli New Sheqel" to "ILS",
    "IMP : Manx pound" to "IMP",
    "INR : Indian Rupee" to "INR",
    "IQD : Iraqi Dinar" to "IQD",
    "IRR : Iranian Rial" to "IRR",
    "ISK : Icelandic Króna" to "ISK",
    "JEP : Jersey Pound" to "JEP",
    "JMD : Jamaican Dollar" to "JMD",
    "JOD : Jordanian Dinar" to "JOD",
    "JPY : Japanese Yen" to "JPY",
    "KES : Kenyan Shilling" to "KES",
    "KGS : Kyrgystani Som" to "KGS",
    "KHR : Cambodian Riel" to "KHR",
    "KMF : Comorian Franc" to "KMF",
    "KPW : North Korean Won" to "KPW",
    "KRW : South Korean Won" to "KRW",
    "KWD : Kuwaiti Dinar" to "KWD",
    "KYD : Cayman Islands Dollar" to "KYD",
    "KZT : Kazakhstani Tenge" to "KZT",
    "LAK : Laotian Kip" to "LAK",
    "LBP : Lebanese Pound" to "LBP",
    "LKR : Sri Lankan Rupee" to "LKR",
    "LRD : Liberian Dollar" to "LRD",
    "LSL : Lesotho Loti" to "LSL",
    "LTL : Lithuanian Litas" to "LTL",
    "LVL : Latvian Lats" to "LVL",
    "LYD : Libyan Dinar" to "LYD",
    "MAD : Moroccan Dirham" to "MAD",
    "MDL : Moldovan Leu" to "MDL",
    "MGA : Malagasy Ariary" to "MGA",
    "MKD : Macedonian Denar" to "MKD",
    "MMK : Myanma Kyat" to "MMK",
    "MNT : Mongolian Tugrik" to "MNT",
    "MOP : Macanese Pataca" to "MOP",
    "MRO : Mauritanian Ouguiya" to "MRO",
    "MUR : Mauritian Rupee" to "MUR",
    "MVR : Maldivian Rufiyaa" to "MVR",
    "MWK : Malawian Kwacha" to "MWK",
    "MXN : Mexican Peso" to "MXN",
    "MYR : Malaysian Ringgit" to "MYR",
    "MZN : Mozambican Metical" to "MZN",
    "NAD : Namibian Dollar" to "NAD",
    "NGN : Nigerian Naira" to "NGN",
    "NIO : Nicaraguan Córdoba" to "NIO",
    "NOK : Norwegian Krone" to "NOK",
    "NPR : Nepalese Rupee" to "NPR",
    "NZD : New Zealand Dollar" to "NZD",
    "OMR : Omani Rial" to "OMR",
    "PAB : Panamanian Balboa" to "PAB",
    "PEN : Peruvian Nuevo Sol" to "PEN",
    "PGK : Papua New Guinean Kina" to "PGK",
    "PHP : Philippine Peso" to "PHP",
    "PKR : Pakistani Rupee" to "PKR",
    "PLN : Polish Zloty" to "PLN",
    "PYG : Paraguayan Guarani" to "PYG",
    "QAR : Qatari Rial" to "QAR",
    "RON : Romanian Leu" to "RON",
    "RSD : Serbian Dinar" to "RSD",
    "RUB : Russian Ruble" to "RUB",
    "RWF : Rwandan Franc" to "RWF",
    "SAR : Saudi Riyal" to "SAR",
    "SBD : Solomon Islands Dollar" to "SBD",
    "SCR : Seychellois Rupee" to "SCR",
    "SDG : Sudanese Pound" to "SDG",
    "SEK : Swedish Krona" to "SEK",
    "SGD : Singapore Dollar" to "SGD",
    "SHP : Saint Helena Pound" to "SHP",
    "SLL : Sierra Leonean Leone" to "SLL",
    "SOS : Somali Shilling" to "SOS",
    "SRD : Surinamese Dollar" to "SRD",
    "STD : São Tomé and Príncipe Dobra" to "STD",
    "SVC : Salvadoran Colón" to "SVC",
    "SYP : Syrian Pound" to "SYP",
    "SZL : Swazi Lilangeni" to "SZL",
    "THB : Thai Baht" to "THB",
    "TJS : Tajikistani Somoni" to "TJS",
    "TMT : Turkmenistani Manat" to "TMT",
    "TND : Tunisian Dinar" to "TND",
    "TOP : Tongan Paʻanga" to "TOP",
    "TRY : Turkish Lira" to "TRY",
    "TTD : Trinidad and Tobago Dollar" to "TTD",
    "TWD : New Taiwan Dollar" to "TWD",
    "TZS : Tanzanian Shilling" to "TZS",
    "UAH : Ukrainian Hryvnia" to "UAH",
    "UGX : Ugandan Shilling" to "UGX",
    "USD : United States Dollar" to "USD",
    "UYU : Uruguayan Peso" to "UYU",
    "UZS : Uzbekistan Som" to "UZS",
    "VEF : Venezuelan Bolívar Fuerte" to "VEF",
    "VND : Vietnamese Dong" to "VND",
    "VUV : Vanuatu Vatu" to "VUV",
    "WST : Samoan Tala" to "WST",
    "XAF : CFA Franc BEAC" to "XAF",
    "XAG : Silver (troy ounce)" to "XAG",
    "XAU : Gold (troy ounce)" to "XAU",
    "XCD : East Caribbean Dollar" to "XCD",
    "XDR : Special Drawing Rights" to "XDR",
    "XOF : CFA Franc BCEAO" to "XOF",
    "XPF : CFP Franc" to "XPF",
    "YER : Yemeni Rial" to "YER",
    "ZAR : South African Rand" to "ZAR",
    "ZMK : Zambian Kwacha (pre-2013)" to "ZMK",
    "ZMW : Zambian Kwacha" to "ZMW",
    "ZWL : Zimbabwean Dollar" to "ZWL"
)