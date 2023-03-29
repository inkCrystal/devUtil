package cn.dev.commons.verification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;

class VerificationToolTest {


    @Test
    void isNotNull() {
        Assertions.assertFalse(VerificationHelper.isNotNull(null).isValid());
        Assertions.assertTrue(VerificationHelper.isNotNull("").isValid());
    }

    @Test
    void collectionIsNotEmpty() {
        assert !VerificationHelper.collectionIsNotEmpty(null).isValid();
        assert !VerificationHelper.collectionIsNotEmpty(new ArrayList()).isValid();
        Assertions.assertTrue( VerificationHelper.collectionIsNotEmpty(List.of("","")).isValid());
    }

    @Test
    void arrayIsNotEmpty() {
        assert !VerificationHelper.arrayIsNotEmpty(null).isValid();
        assert !VerificationHelper.arrayIsNotEmpty(new String[]{}).isValid();
        assert VerificationHelper.arrayIsNotEmpty( new String[]{"", ":"}).isValid();
    }

    @Test
    void mapContainsKey() {
        Map map = Map.of("k","b");
        assert VerificationHelper.mapContainsKey(map, "k").isValid();
        assert !VerificationHelper.mapContainsKey(null, "k").isValid();
        assert !VerificationHelper.mapContainsKey(new HashMap(), "b").isValid();
        assert !VerificationHelper.mapContainsKey(map, "b").isValid();
    }

    @Test
    void mapIsNotEmpty() {
        Map map =new HashMap();
        map.put("k",1);
        assert VerificationHelper.mapIsNotEmpty(map).isValid();
        assert !VerificationHelper.mapIsNotEmpty(null).isValid();
        assert !VerificationHelper.mapIsNotEmpty(Map.of()).isValid();
    }

    @Test
    void isNotMatch() {
        assert !VerificationHelper.isNotMatch(1, (x) -> x > 0).isValid();
        assert !VerificationHelper.isNotMatch(null, (x) -> x.equals(1)).isValid();
        assert VerificationHelper.isNotMatch(1, x -> x < 0).isValid();
    }

    @Test
    void isMatch() {
        assert VerificationHelper.isMatch(1, x -> x == 1).isValid();
        assert !VerificationHelper.isMatch(1, x -> x > 1).isValid();
        assert !VerificationHelper.isMatch(null, x -> x.equals(1)).isValid();
    }
}