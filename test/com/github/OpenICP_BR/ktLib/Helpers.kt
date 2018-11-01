package com.github.OpenICP_BR.ktLib

fun newTestRootCA(): KeyAndCert {
    val builder = KeyAndCertBuilder()
    builder.isRoot = true
    builder.isCA = true
    return builder.build()
}

fun newCert(subjectCN: String, ca: KeyAndCert): KeyAndCert {
    val builder = KeyAndCertBuilder()
    builder.subjectCN = subjectCN
    builder.issuer = ca
    return builder.build()
}