package com.github.OpenICP_BR.ktLib

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.asn1.x509.*
import org.bouncycastle.cert.X509v3CertificateBuilder
import org.bouncycastle.operator.ContentSigner
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.cryptacular.generator.KeyPairGenerator
import java.math.BigInteger
import java.security.KeyPair
import java.security.SecureRandom
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

val TESTING_ROOT_CA_SUBJECT_C = "BR"
val TESTING_ROOT_CA_SUBJECT_O = "FakeICP - SEM VALOR LEGAL"
val TESTING_ROOT_CA_SUBJECT_CN = "Fake AC Raíz"
val TESTING_ROOT_CA_SUBJECT_OU = ""
val TESTING_ROOT_CA_SUBJECT_EMAIL = ""
val TESTING_ROOT_CA_SUBJECT = "C=$TESTING_ROOT_CA_SUBJECT_C, O=$TESTING_ROOT_CA_SUBJECT_O, CN=$TESTING_ROOT_CA_SUBJECT_CN"
