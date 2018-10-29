package com.github.OpenICP_BR.ktLib

abstract class ICPException: Exception() {
    abstract val msg_en : String
    abstract val msg_pt : String
    abstract val code : String
}

abstract class ICPExceptionWithEncapsulation: ICPException() {
    abstract val exception : Exception
}

class FailedCertificateParsing(override val exception: Exception) : ICPExceptionWithEncapsulation() {
    override val code = "FAILED_TO_PARSE_CERT"
    override val msg_en = "failed to parse certificate"
    override val msg_pt  = "falha ao analisar certificado"
}