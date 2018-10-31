package com.github.OpenICP_BR.ktLib

open class ICPException(
        open val code: String,
        open val msg_en : String,
        open val msg_pt : String
    ) : Exception(msg_en) {
}

open class ICPExceptionWithEncapsulation(
        open val exception: Exception,
        override val code: String,
        override val msg_en: String,
        override val msg_pt: String
    ): ICPException(code, msg_en, msg_pt) {
}

class FailedCertificateParsingException(override val exception: Exception) : ICPExceptionWithEncapsulation(
        exception,
        "FAILED_TO_PARSE_CERT",
        "failed to parse certificate",
        "falha ao analisar certificado") {
}

class IllegalTestingRootCA: ICPException(
        "ILLEGAL_TESTING_ROOT_CA_SUBJECT",
        "the testing root CA MUST have the subject and issuer: $TESTING_ROOT_CA_SUBJECT",
        "a AC raíz DEVE ter como titular e emissor: $TESTING_ROOT_CA_SUBJECT") {
}

class FailedToReadCertificateException(override val exception: Exception) : ICPExceptionWithEncapsulation(
        exception,
        "FAILED_TO_READ_CERT",
        "failed to read certificate",
        "falha ao ler certificado") {
}