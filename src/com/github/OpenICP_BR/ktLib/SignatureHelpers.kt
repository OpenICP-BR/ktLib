package com.github.OpenICP_BR.ktLib

import org.bouncycastle.cms.CMSSignedData
import java.io.File

fun LoadOneSignature(data: ByteArray): Signature {
    return Signature(CMSSignedData(data))
}

fun LoadOneSignature(path: String): Signature {
    return LoadOneSignature(File(path).readBytes())
}