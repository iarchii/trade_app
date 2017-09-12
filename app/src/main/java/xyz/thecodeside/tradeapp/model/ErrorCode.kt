package xyz.thecodeside.tradeapp.model


enum class ErrorCode {
    TRADING_002, //("unexpected error"),
    AUTH_007, //("access token is not valid"),
    AUTH_014, //("user does not have sufficient permissions to perform this action"),
    AUTH_009,// ("missing Authorization header"),
    AUTH_008, //("access token is expired"),
    RTF_002, //("missing JWT Access Token in request")
}