package app.sixarmstudios.adventofcode2018

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Day 5
 *
 * Polymers!
 *
 * Units' types are represented by letters; units' polarity is represented by capitalization.
 * For instance, r and R are units with the same type but opposite polarity, whereas r and s
 * are entirely different types and do not react.
 *
 * Starting at 8:20am
 *
 */
class Day5 {
    private fun part1_shouldDisapear(left: String, right: String): Boolean {
        return left != right && left.toLowerCase() == right.toLowerCase()
    }

    data class ChainBuilder(val hasReaction: Boolean, val prev: String, val soFar: MutableList<String>)

    private fun part1_chainReaction(chain: String): String {
        var polymer = listOf<String>()
        var stablePolymer = chain.split("")
                .filter { it.isNotBlank() }
        return part1_chainSplit(mutableListOf(), stablePolymer.toMutableList())
//        while (polymer != stablePolymer) {
//            polymer = stablePolymer
//            stablePolymer = polymer.fold(ChainBuilder(false, "", mutableListOf()), this::part1_folder).soFar
//        }
//        return stablePolymer.joinToString("")
    }

    private fun part2_chainReaction(chain: String): Int {
        val stablePolymer = chain.split("")
                .filter { it.isNotBlank() }
        return "abcdefghijklmnopqrstuvwxyz"
                .split("")
                .filter { it.isNotBlank() }
                .map { letter ->
                    stablePolymer.filter { it.toLowerCase() != letter.toLowerCase() }
                }
                .map {
                    part1_chainSplit(mutableListOf(), it.toMutableList()).length
                }
                .min()!!
    }

    tailrec fun part1_chainSplit(left: MutableList<String>, right: MutableList<String>): String {
        if (right.isEmpty()) {
            return left.joinToString("")
        }
        val runit = right.removeAt(0)
        if (left.isEmpty()) {
            left.add(runit)
            return part1_chainSplit(left, right)
        }
        val lunit = left.last()
        if (part1_shouldDisapear(lunit, runit)) {
            left.removeAt(left.size - 1)
            return part1_chainSplit(left, right)
        }
        left.add(runit)
        return part1_chainSplit(left, right)
    }

    private fun part1_folder(soFar: ChainBuilder, unit: String): ChainBuilder {
        return when {
            soFar.hasReaction -> {
                soFar.soFar.add(unit)
                ChainBuilder(true, "", soFar.soFar)
            }
            part1_shouldDisapear(soFar.prev, unit) -> {
                soFar.soFar.removeAt(soFar.soFar.size - 1)
                ChainBuilder(true, "", soFar.soFar)
            }
            else -> {
                soFar.soFar.add(unit)
                ChainBuilder(false, unit, soFar.soFar)
            }
        }
    }

    @Test
    fun test_part1_folder1() {
        assertThat(part1_folder(ChainBuilder(true, "z", mutableListOf("y")), "j"), `is`(ChainBuilder(true, "", mutableListOf("y", "j"))))
    }

    @Test
    fun test_part1_folder2() {
        assertThat(part1_folder(ChainBuilder(false, "z", mutableListOf("y")), "j"), `is`(ChainBuilder(false, "j", mutableListOf("y", "j"))))
    }

    @Test
    fun test_part1_folder3() {
        assertThat(part1_folder(ChainBuilder(false, "z", mutableListOf("y", "M", "Z", "k", "z")), "Z"), `is`(ChainBuilder(true, "", mutableListOf("y", "M", "Z", "k"))))
    }

    @Test
    fun test_part1_folder4() {
        assertThat(part1_folder(ChainBuilder(false, "", mutableListOf()), "a"), `is`(ChainBuilder(false, "a", mutableListOf("a"))))
    }

    @Test
    fun test_part1_folder5() {
        assertThat(part1_folder(ChainBuilder(false, "a", mutableListOf("a")), "A"), `is`(ChainBuilder(true, "", mutableListOf())))
    }

    @Test
    fun test_part1_so_sad() {
        assertThat(part1_folder(ChainBuilder(false, "a", mutableListOf("a")), "A"), `is`(ChainBuilder(true, "", mutableListOf())))
    }

    @Test
    fun rebecca_doesnt_know_how_substring_works() {
        val shark = "sharkz"
        assertThat(shark.substring(0, shark.length - 1), `is`("shark"))
    }

    @Test
    fun part1_react_singular1() {
        assertThat(part1_shouldDisapear("a", "A"), `is`(true))
    }

    @Test
    fun part1_react_singular2() {
        assertThat(part1_shouldDisapear("A", "a"), `is`(true))
    }

    @Test
    fun part1_react_singular3() {
        assertThat(part1_shouldDisapear("a", "s"), `is`(false))
    }

    @Test
    fun part1_react_singular4() {
        assertThat(part1_shouldDisapear("a", "a"), `is`(false))
    }

    @Test
    fun part1_react_singular5() {
        assertThat(part1_shouldDisapear("a", "B"), `is`(false))
    }

    @Test
    fun part1_react1() {
        assertThat(part1_chainReaction("zAa"), `is`("z"))
    }

    @Test
    fun part1_react2() {
        assertThat(part1_chainReaction("abBA"), `is`(""))
    }

    @Test
    fun part1_react3() {
        assertThat(part1_chainReaction("abAB"), `is`("abAB"))
    }

    @Test
    fun part1_react4() {
        assertThat(part1_chainReaction("aabAAB"), `is`("aabAAB"))
    }

    @Test
    fun part1_react5() {
        assertThat(part1_chainReaction("dabAcCaCBAcCcaDA"), `is`("dabCBAcaDA"))
    }

    @Test
    fun part1_react6() {
        assertThat(part1_chainReaction("Aaz"), `is`("z"))
    }

    @Test
    fun solution_part1() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day5_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        // 50000 characters long...  ^^;
        assertThat(part1_chainReaction(inputStr).length, `is`(9116))
        // incorrect guess @ 9:13am : JoMwlRXCyoPTmXurdjiMfzpGqidunFKAsqEKqdBzKavGfgscBMiWJvUyTGbmayETZcfMfEFMdHdSBfdOGjWkQUTSZvjrOBEBhvInlwrUPoTTWzdMHUgjYrIVURtGAUfnKlVfSUaoDBZrvqVlqFVsPapsHokvJKxPYklKKRkTrPDpptElYKjtgOmykurKIRzXqkxsRGwqVLORihXjlvJqzLIzjzVBeDlpmhbHPYbDxTBlHOEDODKYRdFOMXCQAImZybHrbidwutdFRTvzSJRnnMuTuehUBpJFixrpmcyUhkxMHJHNhlTCmimyTcvnPRiJJOPveVLEstElQxfxsiCxFVQTpFUgoynqRyBnkuiAtPNQUPESkhdZpLhbUxRReBeNyVJuyklAEJFFsgnLujxcbgcamfaxBhtPnXbfCaYVAPwoLQguyQMKLxvsUwMHgSOWfmjOiPSBqfoHGsdGAkhTMwGsdkRtEGHQpOYUyrFsySeZjzFkwklTyLmvLJrCNagnAlnYYDPOujnYjdwVYHvnQDievPPLXZHvTmQVPfvuHUUhXFntEgbVYFkvxruGpSAgPZLuBRMOvKxvJTEyUUOfJHuyCwHDjeoTYiUlTKSivjgMRNkGvGRodeeHuzjzjssyUKmDJUHDnhZsJMPspOLizBpWZVjzoUAGEgMNfPPblYjjaxxmEGqugLdUWIdaVSEziYRIJgSiaoEjkLXBvItDvZcyiNobZBsiuYbxYRUOZTCwqTkySlemNiGzDBXTMXlEcGoDQSjEtgitOIcNKcBplDSvlthhFSjMXKyXrOaCrCdTTuyPVyoSmXdOJofAvaBqRXjkOJpzxTksrFqjHWhEbrJDwsNkyRcaSAxWgKRsewEHzxacfLquILNVdZORlqRNrCVgDTxRAzyWkronKKAFbNQTSgOJrZTmFBokbnevGTxOMjfeKgjTUPSzEakjCpmJCaEoqgyjqXbajinCXjenpvYAUTIaYvkMXMUHTllxkksrVptbGVPvkQpMcnWDxUPdBrKrwoClvRwZIBsFrPPLQRoiXKJxWOWayqnJmoWdRUCtFhPABfxNWUothZHknipsZZsAFpYfnKHjYWabVJpSWHnqpMsjtjvTVNiJxDbeSIPTIaglgOhDZHUVTrCpNWpOEPQOgtcqDkMaTrDDJaCIovHNIWMcnBWmyAxafqGCNwFiJROamhVqdLjFPDuVxAVGZDpcPRodOjzwtkAFOLWyEvtnieUrpyRHqRKJnuXTogZMkehlNfBYNqoNfxbGRtcNltwjdaipyXKmcQQmJeZysbOQOaVqkqifUoIbWieJLhkOvsEJCFewvkqOALupQNPeUOrGPzsoTWKjXzwndnCnMAHASWcbwCSySWSyAULXIrwBEbkBmaQMleehVckxzBeyjEORhFsYiodacsDzCeTGCdfbxEciojpkJkPelCzLKoCBciKGWthiHHffUGYFZJOkvYxeiVjDpdlpcUjhmibVfCELkGkBFMOKpeCsuAKnAyjMlWrDyDacfcXYVYYGXpMotVClSALOVqrMGQAhUbzALikiTjZXExClolyuQVPupvchPjLWQaMCpKUgZTlwHSmCAkbnyQwyhpHWFFlbGsTahJHQsXZadULcsyTnszTybZZpzvrjAzRUivPXnOuXkHWgOcRWIFAkUwRlMdqOMaEvzhMpKvausDTEJRnOAvEZiGyBhLsWLYHxsMANNtVlmIwEEoQACvBeDZreyRQruvkwVVXjWTYkQnTrJmImTaElVQfHMGIxagJqjnQZsdyAIJcBAShUywzwFHdTZunrwFDwQEmYtnnoUwMoIJNZpjbSHruxseBdrCTrzBOFgpAZdEqXuWzxVAqOPoxCXJnclWtJKYfQrneYaZnWQzqCrKqKdVWoTcpnPDuXoPMLOSKlMxyRWvmuYJhUwwtcwVndrfPHeSzaBxOwQnwifLTawdxUCkIyGcqnLVGccEBsFjYFqLWJSxYYPkVxhluEEcNjbLRIFSCgRjhvXpNsTnegbtYCHyMAexmgKcRXUjNFSkmQcMxgrqPgozgmKooSAQtcgQJedfSQSaemNjOVnRQrmFbtkQkooanUWmSXMNtlDPljOhRiBESdtBaGHjOeBZUmfJmbZSgKHByQSznmPKDmBDEvEQJceowSUIIJXvlafRPvrVYcLVREcIQdselkGfuABoszHdxdQLPkahvPeAFIfSzMEHsCzOgNslQlqeXZFKtuLyySzaIMPxHKftBfvnLCBmTmLSzTWekPiNrruOakoeftaMVDLsLkdutWgdhxydpdhlOgPYJThlVdKtxuKSHyNrKfMbfymkVohnyWTdWRTXsDuyQkRhAmVntBGnSlXDBqKTDtwOMVeMDjtfpkOiGHmidLkAbAJAiXTgfmhcfYpxjGYKSfjwtfEXhiKBRWAMCGzVFEWBQgteVQSdqJiWYuLroQPFxVWfZiwcdvtUFOADWzJRdGqRYNuqPHrESTLmVwzqxQYweUcSSOeoyeqvCKOZAHtYEPOfBhgXfTdeZneAzKybQdZTbbTlpBDEMccMhavRcvnTxYeQCiTwDhNLrjkkxRBmqXbrJZggEQemTPfUHkHCCrcgnjufEtFsFyTbTXlzrXCNgdWyPsLhaPVPJNuFznumDIjFGuySUwXIsLYCLVwIpQvPBDMWRHAGEZdIoAUOjeTPXGniWASXIkTnvikTdafgflegSYqqDkpkogSHmJykJEZcAldRFVqpfuhgJUFZqnhYnZbUcRRIxRwpmPFQyjlWHFxxOjjxKFDrAKWDRZVFlvhokmByOgjevhcDVeMRoTMVQhmATHurBysTKcgYRLBRzEsNoLRFDlnXgmOaWiRFGqjdhKQdjFdbMdTixLjOHLRfLcNKUeIRzbZANHCXmEQPJTbPWpbFoslHDPylyWhlzrFcjBYzCdFaWIjmEYumJEvEsNEfOiPchQoVUyJdmUbwsVylCsvGHAKPTvhFBgLHyoZjrirsYAiZvMzsiwXvtCTbhUaOaQsjjkMGxCNmSqGUwVgFNOuOtczCKSalrPTWUfSMinUPrsOSLdnTjKNVUBfEynrwEmmyqigMgHdlEVZODywljpSHuKpulmFrTDweuDDwxFWyBLPOUUSmEzEigneUKmpMEooRAvoNJULawgCfyKseIqDpzpxrQazBKiXCCENtyQsmVTanxFGEjejxbkLHPXFXrsPcqiWKhIuRENJKmxmCNpnTFzGXQTokmscDgDesxdRmdZDGlABJafqtXwnLSDZoGqGMVtFmbQNOakMpsqGunLaGCCKfXalIbmDVEDTplMpCvBykRSgVJUPujncuAKrNknRDrAGhOVlyInadGCfbgWzLFOHsliSCWSfnPkGJzANaZLpiaappEvYFkzsTPSyPfOvrdQeBnlJZWThoqkkiDgFDJXLrTPNFprlabarrzvJIqOGYFzHJnzVFzUnRMZhnWMEOKaiXaKTPIUTMWydlaBTruPbnWdpGKwfoUQRwRdSImpGqKRDmDWIcUooldYoTFpUIkcqSjEITaoRWsgSUZXDncjHDaNtiKNCwprowLSPFWqjWcOgDnvhvlwIQZlGZQIsdSAhxdJsMCyzeJvRghGyDzBQkedttySHCtHPXgCimwAwAMewHNBRePrklfEGKHqqOnpIjHEZucZsjeeMugZDfwDJtkPxUqiAAZSbbNskZVEjdhNcHPFDaGpCsvFGDzIFUTzxcAFcnDckvaXDWMRVavWEEIGfflcPVgSJastLatHQuzdwLGUUnXFidwySemTxvmeqJloBDxQpNRNLwMrQCCHXVQBKOpInczCpGqBZTHzgrHGzeYBMdHYNYficVPLVRvfWdwyZLLtnGvASPBBvhFxTOvwgovKULpgVuBUVpINZftpLhvFNTlWdKcmFXnHLXgyMlFZLfuPYkdWmGqIcGTSEoBQWqRexNHTVNRSXRYOhsQFPCIOKImzwfcoYTNKUIrjIqhArWTsmwXKvYWJephxTezroVTyMeuAhzmulcFdGtiEhSnwhNMpHOlDjHBEROABUQSKsNQIxzGHbyFDEGVJEFrKGTdxglbIrSqeXpfpcXyGCHvOfmACroMuMOgntKZbDySgEDjviDVNAlURvmZoZmoyMxHPMvAiTNAAgEfVpOXJJrPmamwFmZuFOXtyNmCNCWAQdiUqibxNDNtqLiFvktQPeIJNaGtfaDkbEOLVNgbUkuTmjRNWjhMVRXlkxoEQCqlKtUgNSLztWvkUrZrQZErfzdKhYGcsYqESFkVqkuyAgfdtlGHQAmvxzDbxZuNMPcasGHNioDuIGOhUQwVeRbMdwhgnWKmURxeXqMXSMSXNikIjDQAFIWQYteMRCMusUNHyPujIlENzJCbfOaxEWRpkTQIfHRLyBdcVumpnTpjkpdzPfKCHuimBWNsuvSFtSjcRbAVAiLdLQONQmvxAiHiMMWfOVmvlxcozasmTEWOfCSyKgfzXzelUKxonHUCaDRTrnlrQyHqbFAyIkopRaZUtAXXEBtVJIaKVQWaoKZZDjXEGxfukbeLWkAWWDwVZolSLOzvWdwwaKwlEBKUFXgexJdzzkOAwqvkAijvTbexxaTuzArPOKiYafBQhYqRLNRtrdAcuhNOXkuLEZxZFGkYscFowetMSAZOCXLVMvoFwmmIhIaXVMqnoqlDlIavaBrCJsTfsVUSnwbMIUhckFpZDPKJPtNPMUvCDbYlrhFiqtKPrweXAoFBcjZneLiJUpYhnuSUmcrmETyqwifaqdJiKInxsmsxmQxEXruMkwNGHWDmBrEvWquHogiUdOInhgSACpmnUzXBdZXVMaqhgLTDFGaYUKQvKfseQySCgyHkDZFRezqRzRuKVwTZlsnGuTkLQcqeOXKLxrvmHJwnrJMtUKuBGnvloeBKdAFTgAnjiEpqTKVfIlQTndnXBIQuIDqawcncMnYTxofUzMfWMAMpRjjxoPvFeGaantIaVmphXmYOMzOzMVruLanvdIVJdeGsYdBzkTNGomUmORcaMFoVhcgYxCPFPxEQsRiBLGXDtgkRfejvgedfYBhgZXiqnSksqubaorebhJdLohPmnHWNsHeITgDfCLUMZHaUEmYtvORZEtXHPEjwyVkxWMStwRaHQiJRiukntyOCFWZMikoicpfqSHoyrxsrnvthnXErQwqbOestgCiQgMwDKypUFlzfLmYGxlhNxfMCkDwLtnfVHlPTFzniPvubUvGPlukVOGWVotXfHVbbpsaVgNTllzYWDwFVrvlpvCIFynyhDmbyEZghRGZhtzbQgPcZCNiPokbqvxhccqRmWlnrnPqXdbOLjQEMVXtMEsYWDIfxNuuglWDZUqhTAlTSAjsGvpCLFFgieewVAvrmwdxAVKCdNCfaCXZtufiZdgfVScPgAdfphCnHDJevzKSnBBszaaIQuXpKTjdWFdzGUmEEJSzCUzehJiPNoQQhkgeFLKRpErbnhWEmaWaWMIcGxphTchsYTTDEKqbZdYgHGrVjEZYcmSjDXHasDSizgLzqiWLVHVNdGoCwJQwfpslWORPWcnkITnAdhJCNdxzusGSwrOAtieJsQCKiuPftOyDLOOuCiwdMdrkQgPMisDrWrquOFWkgPDwNBpURtbALDYwmtuiptkAxIAkoemwNHzmrNuZfvZNjhZfygoQijVZRRABALRPfnptRlxjdfGdIKKQOHtwzjLNbEqDRVoFpYsptSZKfyVePPAAIPlzAnaZjgKpNFswcsILShoflZwGBFcgDANiYLvoHgaRdrNKnRkaUCNJUpujvGsrKYbVcPmLPtdevdMBiLAxFkccgAlNUgQSPmKAonqBMfTvmgQgOzdslNWxTQFAjbaLgdzDMrDXSEdGdCSMKOtqxgZftNPncMXMkjnerUiHkwIQCpSRxfxphlKBXJEJegfXNAtvMSqYTneccxIkbZAqRXPZPdQiESkYFcGWAlujnOVarOOemPMkuENGIeZeMsuuoplbYwfXWddUEWdtRfMLUPkUhsPJLWYdozveLDhGmGIQYMMeWRNYeFbuvnkJtNDlsoSRpuNImsFuwtpRLAskcZCToUonfGvWugQsMncXgmKJJSqAoAuHBtcTVxWISZmVzIaySRIRJzOYhlGbfHVtpkahgVScLYvSWBuMDjYuvOqHCpIoFenSeVejMUyeMJiwAfDcZybJCfRZLHwYLYpdhLSOfBPwpBtjpqeMxchnazBZriEuknClFrlhoJlXItDmBDfJDqkHDJQgfrIwAoMGxNLdfrlOnSeZrblryGCktSYbRUhtaMHqvmtOrmEvdCHVEJGoYbMKOHVLfvzrdwkaRdfkXJJoXXfhwLJYqfpMPWrXirrCuBzNyHNQzfujGHUFPQvfrDLaCzejKYjMhsGOKPKdQQysGELFGFADtKIVNtKixsawINgxptEJouaOiDzegahrwmdbpVqPiWvlcylSixWusYUgfJidMUNZfUnjpvpAHlSpYwDGncxRZLxtBtYfSfTeFUJNGCRcchKhuFptMEqeGGzjRBxQMbrXKKJRlnHdWtIcqEyXtNVCrVAHmCCmedbPLtBBtzDqBYkZaENzEDtFxGHbFopeyThazokcVQEYOEossCuEWyqXQZWvMltseRhpQUnyrQgDrjZwdaofuTVDCWIzFwvXfpqORlUywIjQDsqvETGqbwefvZgcmawrbkIHxeFTWJFskygJXPyFCHMFGtxIajaBaKlDIMhgIoKPFTJdmEvmoWTdtkQbdxLsNgbTNvMaHrKqYUdSxtrwDtwYNHOvKMYFBmFkRnYhskUXTkDvLHtjypGoLHDPDYXHDGwTUDKlSldvmATFEOKAoURRnIpKEwtZslMtMbclNVFbTFkhXpmiAZsYYlUTkfzxEQLqLSnGoZcShemZsFifaEpVHAKplqDXDhZSObaUFgKLESDqiCervlCyvRVprFALVxjiiusWOECjqeVedbMdkpMNZsqYbhkGszBMjFMuzbEoJhgAbTDsebIrHoJLpdLTnmxsMwuNAOOKqKTBfMRqrNvoJnMEAsqsFDEjqGCTqasOOkMGZOGpQRGXmCqMKsfnJuxrCkGMXEamYhcyTBGENtSnPxVHJrGcsfirlBJnCeeULHXvKpyyXsjwlQfyJfSbeCCgvlNQCgYiKcuXDWAtlFIWNqWoXbAZsEhpFRDNvWCTWWuHjyUMVwrYXmLksolmpOxUdpNPCtOwvDkQkRcQZqwNzAyENRqFykjTwLCNjxcXOpoQavXZwUxQeDzaPGfobZRtcRDbESXURhsBJPznjiOmWuONNTyMeqWdfWRNUztDhfWZWYuHsabCjiaYDSzqNJQjGAXigmhFqvLeAtMiMjRtNqKytwJxvvWKVURqrYERzdEbVcaqOeeWiMLvTnnamSXhylwSlHbYgIzeVaoNrjetdSUAVkPmHZVeAmoQDmLrWuKafiwrCoGwhKxUoNxpVIurZaJRVZPzzBYtZSNtYSCluDAzxSqhjHAtSgBLffwhPHYWqYNBKacMshWLtzGukPcmAqwlJpHCVPUpvqUYLOLcXexzJtIKIlaZBuHaqgmRQvolasLcvTOmPxgyyvyxCFCAdYdRwLmJYaNkaUScEPkomfbKgKlecFvBIMHJuCPLDPdJvIEXyVKojzfyguFFhhIHTwgkIOklZcLEpKjKPJOICeXBFDcgtEcZdSCADOIySfHroeJYEbZXKCvHEELmqAMbKBebWRixluaYswsYscWBCwsahamNcNDNWZxJkwtOZpgRouEpnqPUlaoQKVWEfcjeSVoKHljEIwBiOuFIQKQvAoqoBSYzEjMqqCMkxYPIADJWTLnCTrgBXFnOQnybFnLHEKmzGOtxUNjkrQhrYPRuEINTVeYwlofaKTWZJoDOrpCPdzgvaXvUdpfJlDQvHMAorjIfWncgQFAXaYMwbNCmwinhVOicAjddRtAmKdQCTGoqpeoPwnPcRtvuhzdHoGLGAitpisEBdXjInvtVJTJSmPQNhwsPjvBAwyJhkNFyPfaSzzSPINKhzHTOuwnXFbapHfTcurDwOMjNQYAwowXjkxIOrqlppRfSbizWrVLcOWRkRbDpuXdwNCmPqKVpvgBTPvRSKKXLLthumxmKVyAituayVPNEJxcNIJABxQJYGQOeAcjMPcJKAeZsputJGkEFJmoXtgVENBKObfMtzRjoGstqnBfakkNORKwYZarXtdGvcRnrQLrozDvnliUQlFCAXZheWESxfmqMFXrkGwXasACrYKnSWdjRBeHwhJQfRSKtXZPjoKJxrQbAVaFOjoDxMsOYvpYUttDcRcAoRxYkxmJsfHHTLVsdLPbCknCioTIGTeJsqdOgCeLxmtxbdZgInMELsYKtQWctzouryXByUISbzBOnIYCzVdTiVbxlKJeOAIsGjiryIZesvADiwuDlGUQgeMXXAJJyLBppFnmGegauOZJvzwPbZIloPSpmjSzHNdhujdMkuYSSJZJZUhEEDOrgVgKnrmGJVIsktLuIytOEJdhWcYUhjFouuYetjVXkVomrbUlzpGasPgURXVKfyvBGeTNfxHuuhUVFpvqMtVhzxlppVEIdqNVhyvWDJyNJUopdyyNLaNGAncRjlVMlYtLKWKfZJzEsYSfRYuyoPqhgeTrKDSgWmtHKagDSghOFQbspIoJMFwosGhmWuSVXlkmqYUGqlOWpavyAcFBxNpTHbXAFMACGBCXJUlNGSffjeaLKYUjvYnEbErrXuBHlPzDHKsepuqnpTaIUKNbYrQNYOGufPtqvfXcISXFXqLeTSelvEVpojjIrpNVCtYMIMctLHnhjhmXKHuYCMPRXIfjPbuHEUtUmNNrjsZVtrfDTUWDIBRhBYzMiaqcxmofDrykdodeohLbtXdBypMPLdEbvZJZilZQjVLJxHIrolvQWgrSXKQxZrikRUKYMoGTJkyLeTPPdpRtKrkkLKypXkjVKOhSPApSvfQLvQVRzbdOAusFvLkNFuagTruviRyJGuhmDZwttOpuRWLNiVHbeboRJVzstuqKwJgoDFbsDhDmfeFmFCzteYAMBgtYuVjwImbCSGFgVAkZbDQkeQSakfNUDIQgPZFmIJDRUxMtpOYcxrLWmOj
        //      too slow! took 30sec, was wrong
        // incorrect guess @ 6:59pm : JoMwlRXCyoPTmXurdjiMfzpGqidunFKAsqEKqdBzKavGfgscBMiWJvUyTGbmayETZcfMfEFMdHdSBfdOGjWkQUTSZvjrOBEBhvInlwrUPoTTWzdMHUgjYrIVURtGAUfnKlVfSUaoDBZrvqVlqFVsPapsHokvJKxPYklKKRkTrPDpptElYKjtgOmykurKIRzXqkxsRGwqVLORihXjlvJqzLIzjzVBeDlpmhbHPYbDxTBlHOEDODKYRdFOMXCQAImZybHrbidwutdFRTvzSJRnnMuTuehUBpJFixrpmcyUhkxMHJHNhlTCmimyTcvnPRiJJOPveVLEstElQxfxsiCxFVQTpFUgoynqRyBnkuiAtPNQUPESkhdZpLhbUxRReBeNyVJuyklAEJFFsgnLujxcbgcamfaxBhtPnXbfCaYVAPwoLQguyQMKLxvsUwMHgSOWfmjOiPSBqfoHGsdGAkhTMwGsdkRtEGHQpOYUyrFsySeZjzFkwklTyLmvLJrCNagnAlnYYDPOujnYjdwVYHvnQDievPPLXZHvTmQVPfvuHUUhXFntEgbVYFkvxruGpSAgPZLuBRMOvKxvJTEyUUOfJHuyCwHDjeoTYiUlTKSivjgMRNkGvGRodeeHuzjzjssyUKmDJUHDnhZsJMPspOLizBpWZVjzoUAGEgMNfPPblYjjaxxmEGqugLdUWIdaVSEziYRIJgSiaoEjkLXBvItDvZcyiNobZBsiuYbxYRUOZTCwqTkySlemNiGzDBXTMXlEcGoDQSjEtgitOIcNKcBplDSvlthhFSjMXKyXrOaCrCdTTuyPVyoSmXdOJofAvaBqRXjkOJpzxTksrFqjHWhEbrJDwsNkyRcaSAxWgKRsewEHzxacfLquILNVdZORlqRNrCVgDTxRAzyWkronKKAFbNQTSgOJrZTmFBokbnevGTxOMjfeKgjTUPSzEakjCpmJCaEoqgyjqXbajinCXjenpvYAUTIaYvkMXMUHTllxkksrVptbGVPvkQpMcnWDxUPdBrKrwoClvRwZIBsFrPPLQRoiXKJxWOWayqnJmoWdRUCtFhPABfxNWUothZHknipsZZsAFpYfnKHjYWabVJpSWHnqpMsjtjvTVNiJxDbeSIPTIaglgOhDZHUVTrCpNWpOEPQOgtcqDkMaTrDDJaCIovHNIWMcnBWmyAxafqGCNwFiJROamhVqdLjFPDuVxAVGZDpcPRodOjzwtkAFOLWyEvtnieUrpyRHqRKJnuXTogZMkehlNfBYNqoNfxbGRtcNltwjdaipyXKmcQQmJeZysbOQOaVqkqifUoIbWieJLhkOvsEJCFewvkqOALupQNPeUOrGPzsoTWKjXzwndnCnMAHASWcbwCSySWSyAULXIrwBEbkBmaQMleehVckxzBeyjEORhFsYiodacsDzCeTGCdfbxEciojpkJkPelCzLKoCBciKGWthiHHffUGYFZJOkvYxeiVjDpdlpcUjhmibVfCELkGkBFMOKpeCsuAKnAyjMlWrDyDacfcXYVYYGXpMotVClSALOVqrMGQAhUbzALikiTjZXExClolyuQVPupvchPjLWQaMCpKUgZTlwHSmCAkbnyQwyhpHWFFlbGsTahJHQsXZadULcsyTnszTybZZpzvrjAzRUivPXnOuXkHWgOcRWIFAkUwRlMdqOMaEvzhMpKvausDTEJRnOAvEZiGyBhLsWLYHxsMANNtVlmIwEEoQACvBeDZreyRQruvkwVVXjWTYkQnTrJmImTaElVQfHMGIxagJqjnQZsdyAIJcBAShUywzwFHdTZunrwFDwQEmYtnnoUwMoIJNZpjbSHruxseBdrCTrzBOFgpAZdEqXuWzxVAqOPoxCXJnclWtJKYfQrneYaZnWQzqCrKqKdVWoTcpnPDuXoPMLOSKlMxyRWvmuYJhUwwtcwVndrfPHeSzaBxOwQnwifLTawdxUCkIyGcqnLVGccEBsFjYFqLWJSxYYPkVxhluEEcNjbLRIFSCgRjhvXpNsTnegbtYCHyMAexmgKcRXUjNFSkmQcMxgrqPgozgmKooSAQtcgQJedfSQSaemNjOVnRQrmFbtkQkooanUWmSXMNtlDPljOhRiBESdtBaGHjOeBZUmfJmbZSgKHByQSznmPKDmBDEvEQJceowSUIIJXvlafRPvrVYcLVREcIQdselkGfuABoszHdxdQLPkahvPeAFIfSzMEHsCzOgNslQlqeXZFKtuLyySzaIMPxHKftBfvnLCBmTmLSzTWekPiNrruOakoeftaMVDLsLkdutWgdhxydpdhlOgPYJThlVdKtxuKSHyNrKfMbfymkVohnyWTdWRTXsDuyQkRhAmVntBGnSlXDBqKTDtwOMVeMDjtfpkOiGHmidLkAbAJAiXTgfmhcfYpxjGYKSfjwtfEXhiKBRWAMCGzVFEWBQgteVQSdqJiWYuLroQPFxVWfZiwcdvtUFOADWzJRdGqRYNuqPHrESTLmVwzqxQYweUcSSOeoyeqvCKOZAHtYEPOfBhgXfTdeZneAzKybQdZTbbTlpBDEMccMhavRcvnTxYeQCiTwDhNLrjkkxRBmqXbrJZggEQemTPfUHkHCCrcgnjufEtFsFyTbTXlzrXCNgdWyPsLhaPVPJNuFznumDIjFGuySUwXIsLYCLVwIpQvPBDMWRHAGEZdIoAUOjeTPXGniWASXIkTnvikTdafgflegSYqqDkpkogSHmJykJEZcAldRFVqpfuhgJUFZqnhYnZbUcRRIxRwpmPFQyjlWHFxxOjjxKFDrAKWDRZVFlvhokmByOgjevhcDVeMRoTMVQhmATHurBysTKcgYRLBRzEsNoLRFDlnXgmOaWiRFGqjdhKQdjFdbMdTixLjOHLRfLcNKUeIRzbZANHCXmEQPJTbPWpbFoslHDPylyWhlzrFcjBYzCdFaWIjmEYumJEvEsNEfOiPchQoVUyJdmUbwsVylCsvGHAKPTvhFBgLHyoZjrirsYAiZvMzsiwXvtCTbhUaOaQsjjkMGxCNmSqGUwVgFNOuOtczCKSalrPTWUfSMinUPrsOSLdnTjKNVUBfEynrwEmmyqigMgHdlEVZODywljpSHuKpulmFrTDweuDDwxFWyBLPOUUSmEzEigneUKmpMEooRAvoNJULawgCfyKseIqDpzpxrQazBKiXCCENtyQsmVTanxFGEjejxbkLHPXFXrsPcqiWKhIuRENJKmxmCNpnTFzGXQTokmscDgDesxdRmdZDGlABJafqtXwnLSDZoGqGMVtFmbQNOakMpsqGunLaGCCKfXalIbmDVEDTplMpCvBykRSgVJUPujncuAKrNknRDrAGhOVlyInadGCfbgWzLFOHsliSCWSfnPkGJzANaZLpiaappEvYFkzsTPSyPfOvrdQeBnlJZWThoqkkiDgFDJXLrTPNFprlabarrzvJIqOGYFzHJnzVFzUnRMZhnWMEOKaiXaKTPIUTMWydlaBTruPbnWdpGKwfoUQRwRdSImpGqKRDmDWIcUooldYoTFpUIkcqSjEITaoRWsgSUZXDncjHDaNtiKNCwprowLSPFWqjWcOgDnvhvlwIQZlGZQIsdSAhxdJsMCyzeJvRghGyDzBQkedttySHCtHPXgCimwAwAMewHNBRePrklfEGKHqqOnpIjHEZucZsjeeMugZDfwDJtkPxUqiAAZSbbNskZVEjdhNcHPFDaGpCsvFGDzIFUTzxcAFcnDckvaXDWMRVavWEEIGfflcPVgSJastLatHQuzdwLGUUnXFidwySemTxvmeqJloBDxQpNRNLwMrQCCHXVQBKOpInczCpGqBZTHzgrHGzeYBMdHYNYficVPLVRvfWdwyZLLtnGvASPBBvhFxTOvwgovKULpgVuBUVpINZftpLhvFNTlWdKcmFXnHLXgyMlFZLfuPYkdWmGqIcGTSEoBQWqRexNHTVNRSXRYOhsQFPCIOKImzwfcoYTNKUIrjIqhArWTsmwXKvYWJephxTezroVTyMeuAhzmulcFdGtiEhSnwhNMpHOlDjHBEROABUQSKsNQIxzGHbyFDEGVJEFrKGTdxglbIrSqeXpfpcXyGCHvOfmACroMuMOgntKZbDySgEDjviDVNAlURvmZoZmoyMxHPMvAiTNAAgEfVpOXJJrPmamwFmZuFOXtyNmCNCWAQdiUqibxNDNtqLiFvktQPeIJNaGtfaDkbEOLVNgbUkuTmjRNWjhMVRXlkxoEQCqlKtUgNSLztWvkUrZrQZErfzdKhYGcsYqESFkVqkuyAgfdtlGHQAmvxzDbxZuNMPcasGHNioDuIGOhUQwVeRbMdwhgnWKmURxeXqMXSMSXNikIjDQAFIWQYteMRCMusUNHyPujIlENzJCbfOaxEWRpkTQIfHRLyBdcVumpnTpjkpdzPfKCHuimBWNsuvSFtSjcRbAVAiLdLQONQmvxAiHiMMWfOVmvlxcozasmTEWOfCSyKgfzXzelUKxonHUCaDRTrnlrQyHqbFAyIkopRaZUtAXXEBtVJIaKVQWaoKZZDjXEGxfukbeLWkAWWDwVZolSLOzvWdwwaKwlEBKUFXgexJdzzkOAwqvkAijvTbexxaTuzArPOKiYafBQhYqRLNRtrdAcuhNOXkuLEZxZFGkYscFowetMSAZOCXLVMvoFwmmIhIaXVMqnoqlDlIavaBrCJsTfsVUSnwbMIUhckFpZDPKJPtNPMUvCDbYlrhFiqtKPrweXAoFBcjZneLiJUpYhnuSUmcrmETyqwifaqdJiKInxsmsxmQxEXruMkwNGHWDmBrEvWquHogiUdOInhgSACpmnUzXBdZXVMaqhgLTDFGaYUKQvKfseQySCgyHkDZFRezqRzRuKVwTZlsnGuTkLQcqeOXKLxrvmHJwnrJMtUKuBGnvloeBKdAFTgAnjiEpqTKVfIlQTndnXBIQuIDqawcncMnYTxofUzMfWMAMpRjjxoPvFeGaantIaVmphXmYOMzOzMVruLanvdIVJdeGsYdBzkTNGomUmORcaMFoVhcgYxCPFPxEQsRiBLGXDtgkRfejvgedfYBhgZXiqnSksqubaorebhJdLohPmnHWNsHeITgDfCLUMZHaUEmYtvORZEtXHPEjwyVkxWMStwRaHQiJRiukntyOCFWZMikoicpfqSHoyrxsrnvthnXErQwqbOestgCiQgMwDKypUFlzfLmYGxlhNxfMCkDwLtnfVHlPTFzniPvubUvGPlukVOGWVotXfHVbbpsaVgNTllzYWDwFVrvlpvCIFynyhDmbyEZghRGZhtzbQgPcZCNiPokbqvxhccqRmWlnrnPqXdbOLjQEMVXtMEsYWDIfxNuuglWDZUqhTAlTSAjsGvpCLFFgieewVAvrmwdxAVKCdNCfaCXZtufiZdgfVScPgAdfphCnHDJevzKSnBBszaaIQuXpKTjdWFdzGUmEEJSzCUzehJiPNoQQhkgeFLKRpErbnhWEmaWaWMIcGxphTchsYTTDEKqbZdYgHGrVjEZYcmSjDXHasDSizgLzqiWLVHVNdGoCwJQwfpslWORPWcnkITnAdhJCNdxzusGSwrOAtieJsQCKiuPftOyDLOOuCiwdMdrkQgPMisDrWrquOFWkgPDwNBpURtbALDYwmtuiptkAxIAkoemwNHzmrNuZfvZNjhZfygoQijVZRRABALRPfnptRlxjdfGdIKKQOHtwzjLNbEqDRVoFpYsptSZKfyVePPAAIPlzAnaZjgKpNFswcsILShoflZwGBFcgDANiYLvoHgaRdrNKnRkaUCNJUpujvGsrKYbVcPmLPtdevdMBiLAxFkccgAlNUgQSPmKAonqBMfTvmgQgOzdslNWxTQFAjbaLgdzDMrDXSEdGdCSMKOtqxgZftNPncMXMkjnerUiHkwIQCpSRxfxphlKBXJEJegfXNAtvMSqYTneccxIkbZAqRXPZPdQiESkYFcGWAlujnOVarOOemPMkuENGIeZeMsuuoplbYwfXWddUEWdtRfMLUPkUhsPJLWYdozveLDhGmGIQYMMeWRNYeFbuvnkJtNDlsoSRpuNImsFuwtpRLAskcZCToUonfGvWugQsMncXgmKJJSqAoAuHBtcTVxWISZmVzIaySRIRJzOYhlGbfHVtpkahgVScLYvSWBuMDjYuvOqHCpIoFenSeVejMUyeMJiwAfDcZybJCfRZLHwYLYpdhLSOfBPwpBtjpqeMxchnazBZriEuknClFrlhoJlXItDmBDfJDqkHDJQgfrIwAoMGxNLdfrlOnSeZrblryGCktSYbRUhtaMHqvmtOrmEvdCHVEJGoYbMKOHVLfvzrdwkaRdfkXJJoXXfhwLJYqfpMPWrXirrCuBzNyHNQzfujGHUFPQvfrDLaCzejKYjMhsGOKPKdQQysGELFGFADtKIVNtKixsawINgxptEJouaOiDzegahrwmdbpVqPiWvlcylSixWusYUgfJidMUNZfUnjpvpAHlSpYwDGncxRZLxtBtYfSfTeFUJNGCRcchKhuFptMEqeGGzjRBxQMbrXKKJRlnHdWtIcqEyXtNVCrVAHmCCmedbPLtBBtzDqBYkZaENzEDtFxGHbFopeyThazokcVQEYOEossCuEWyqXQZWvMltseRhpQUnyrQgDrjZwdaofuTVDCWIzFwvXfpqORlUywIjQDsqvETGqbwefvZgcmawrbkIHxeFTWJFskygJXPyFCHMFGtxIajaBaKlDIMhgIoKPFTJdmEvmoWTdtkQbdxLsNgbTNvMaHrKqYUdSxtrwDtwYNHOvKMYFBmFkRnYhskUXTkDvLHtjypGoLHDPDYXHDGwTUDKlSldvmATFEOKAoURRnIpKEwtZslMtMbclNVFbTFkhXpmiAZsYYlUTkfzxEQLqLSnGoZcShemZsFifaEpVHAKplqDXDhZSObaUFgKLESDqiCervlCyvRVprFALVxjiiusWOECjqeVedbMdkpMNZsqYbhkGszBMjFMuzbEoJhgAbTDsebIrHoJLpdLTnmxsMwuNAOOKqKTBfMRqrNvoJnMEAsqsFDEjqGCTqasOOkMGZOGpQRGXmCqMKsfnJuxrCkGMXEamYhcyTBGENtSnPxVHJrGcsfirlBJnCeeULHXvKpyyXsjwlQfyJfSbeCCgvlNQCgYiKcuXDWAtlFIWNqWoXbAZsEhpFRDNvWCTWWuHjyUMVwrYXmLksolmpOxUdpNPCtOwvDkQkRcQZqwNzAyENRqFykjTwLCNjxcXOpoQavXZwUxQeDzaPGfobZRtcRDbESXURhsBJPznjiOmWuONNTyMeqWdfWRNUztDhfWZWYuHsabCjiaYDSzqNJQjGAXigmhFqvLeAtMiMjRtNqKytwJxvvWKVURqrYERzdEbVcaqOeeWiMLvTnnamSXhylwSlHbYgIzeVaoNrjetdSUAVkPmHZVeAmoQDmLrWuKafiwrCoGwhKxUoNxpVIurZaJRVZPzzBYtZSNtYSCluDAzxSqhjHAtSgBLffwhPHYWqYNBKacMshWLtzGukPcmAqwlJpHCVPUpvqUYLOLcXexzJtIKIlaZBuHaqgmRQvolasLcvTOmPxgyyvyxCFCAdYdRwLmJYaNkaUScEPkomfbKgKlecFvBIMHJuCPLDPdJvIEXyVKojzfyguFFhhIHTwgkIOklZcLEpKjKPJOICeXBFDcgtEcZdSCADOIySfHroeJYEbZXKCvHEELmqAMbKBebWRixluaYswsYscWBCwsahamNcNDNWZxJkwtOZpgRouEpnqPUlaoQKVWEfcjeSVoKHljEIwBiOuFIQKQvAoqoBSYzEjMqqCMkxYPIADJWTLnCTrgBXFnOQnybFnLHEKmzGOtxUNjkrQhrYPRuEINTVeYwlofaKTWZJoDOrpCPdzgvaXvUdpfJlDQvHMAorjIfWncgQFAXaYMwbNCmwinhVOicAjddRtAmKdQCTGoqpeoPwnPcRtvuhzdHoGLGAitpisEBdXjInvtVJTJSmPQNhwsPjvBAwyJhkNFyPfaSzzSPINKhzHTOuwnXFbapHfTcurDwOMjNQYAwowXjkxIOrqlppRfSbizWrVLcOWRkRbDpuXdwNCmPqKVpvgBTPvRSKKXLLthumxmKVyAituayVPNEJxcNIJABxQJYGQOeAcjMPcJKAeZsputJGkEFJmoXtgVENBKObfMtzRjoGstqnBfakkNORKwYZarXtdGvcRnrQLrozDvnliUQlFCAXZheWESxfmqMFXrkGwXasACrYKnSWdjRBeHwhJQfRSKtXZPjoKJxrQbAVaFOjoDxMsOYvpYUttDcRcAoRxYkxmJsfHHTLVsdLPbCknCioTIGTeJsqdOgCeLxmtxbdZgInMELsYKtQWctzouryXByUISbzBOnIYCzVdTiVbxlKJeOAIsGjiryIZesvADiwuDlGUQgeMXXAJJyLBppFnmGegauOZJvzwPbZIloPSpmjSzHNdhujdMkuYSSJZJZUhEEDOrgVgKnrmGJVIsktLuIytOEJdhWcYUhjFouuYetjVXkVomrbUlzpGasPgURXVKfyvBGeTNfxHuuhUVFpvqMtVhzxlppVEIdqNVhyvWDJyNJUopdyyNLaNGAncRjlVMlYtLKWKfZJzEsYSfRYuyoPqhgeTrKDSgWmtHKagDSghOFQbspIoJMFwosGhmWuSVXlkmqYUGqlOWpavyAcFBxNpTHbXAFMACGBCXJUlNGSffjeaLKYUjvYnEbErrXuBHlPzDHKsepuqnpTaIUKNbYrQNYOGufPtqvfXcISXFXqLeTSelvEVpojjIrpNVCtYMIMctLHnhjhmXKHuYCMPRXIfjPbuHEUtUmNNrjsZVtrfDTUWDIBRhBYzMiaqcxmofDrykdodeohLbtXdBypMPLdEbvZJZilZQjVLJxHIrolvQWgrSXKQxZrikRUKYMoGTJkyLeTPPdpRtKrkkLKypXkjVKOhSPApSvfQLvQVRzbdOAusFvLkNFuagTruviRyJGuhmDZwttOpuRWLNiVHbeboRJVzstuqKwJgoDFbsDhDmfeFmFCzteYAMBgtYuVjwImbCSGFgVAkZbDQkeQSakfNUDIQgPZFmIJDRUxMtpOYcxrLWmOj
        //      refactor... took 700ms, still wrong. same answer.
        // OH FUCK, I WAS RIGHT ALL ALONG!! they wanted the length, no the string
    }

    @Test
    fun solution_part2() {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream("day5_input.txt")
        val inputStr = BufferedReader(InputStreamReader(inputStream)).readText()
        inputStream.close()
        assertThat(part2_chainReaction(inputStr), `is`(25000))
        // incorrect guess @ 7:18pm : 25000
        // correct guess @ 7:21pm : 6890

    }
}