/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2023, Huawei Technologies Co., Ltd. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/**
 * @test
 * @bug 8035968
 * @summary Verify that MD5 intrinsic is actually used.
 * @library /testlibrary /testlibrary/whitebox /compiler/testlibrary ../
 * @modules java.base/jdk.internal.misc
 *          java.management
 *
 * @build TestDigest TestMD5Intrinsics intrinsics.Verifier
 * @run main ClassFileInstaller sun.hotspot.WhiteBox
 *                              sun.hotspot.WhiteBox$WhiteBoxPermission
 * @run main/othervm -Xbootclasspath/a:. -XX:+UnlockDiagnosticVMOptions
 *                   -XX:+WhiteBoxAPI -Xbatch -XX:CompileThreshold=500
 *                   -XX:Tier4InvocationThreshold=500
 *                   -XX:+LogCompilation -XX:LogFile=positive.log
 *                   -XX:CompileOnly=sun/security/provider/DigestBase
 *                   -XX:CompileOnly=sun/security/provider/MD5
 *                   -XX:+UseMD5Intrinsics
 *                   -Dalgorithm=MD5
 *                   TestMD5Intrinsics
 * @run main/othervm -Xbootclasspath/a:. -XX:+UnlockDiagnosticVMOptions
 *                   -XX:+WhiteBoxAPI -Xbatch -XX:CompileThreshold=500
 *                   -XX:Tier4InvocationThreshold=500
 *                   -XX:+LogCompilation -XX:LogFile=negative.log
 *                   -XX:CompileOnly=sun/security/provider/DigestBase
 *                   -XX:CompileOnly=sun/security/provider/MD5
 *                   -XX:-UseMD5Intrinsics
 *                   -Dalgorithm=MD5
 *                   TestMD5Intrinsics
 * @run main/othervm -DverificationStrategy=VERIFY_INTRINSIC_USAGE
 *                   intrinsics.Verifier positive.log negative.log
 */


import sha.predicate.IntrinsicPredicates;

public class TestMD5Intrinsics {
    public static void main(String args[]) throws Exception {
        new DigestSanityTestBase(IntrinsicPredicates.isMD5IntrinsicAvailable(),
                DigestSanityTestBase.MD5_INTRINSIC_ID).test();
    }
}
