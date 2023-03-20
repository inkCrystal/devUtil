package cn.dev.commons.verification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;

class VerificationToolTest {


    @Test
    void isNotNull() {
        Assertions.assertFalse(VerificationTool.isNotNull(null).isValid());
        Assertions.assertTrue(VerificationTool.isNotNull("").isValid());
    }

    @Test
    void collectionIsNotEmpty() {
        assert !VerificationTool.collectionIsNotEmpty(null).isValid();
        assert !VerificationTool.collectionIsNotEmpty(new ArrayList()).isValid();
        Assertions.assertTrue( VerificationTool.collectionIsNotEmpty(List.of("","")).isValid());
    }

    @Test
    void arrayIsNotEmpty() {
        assert !VerificationTool.arrayIsNotEmpty(null).isValid();
        assert !VerificationTool.arrayIsNotEmpty(new String[]{}).isValid();
        assert VerificationTool.arrayIsNotEmpty( new String[]{"", ":"}).isValid();
    }

    @Test
    void mapContainsKey() {
        Map map = Map.of("k","b");
        assert VerificationTool.mapContainsKey(map, "k").isValid();
        assert !VerificationTool.mapContainsKey(null, "k").isValid();
        assert !VerificationTool.mapContainsKey(new HashMap(), "b").isValid();
        assert !VerificationTool.mapContainsKey(map, "b").isValid();
    }

    @Test
    void mapIsNotEmpty() {
        Map map =new HashMap();
        map.put("k",1);
        assert VerificationTool.mapIsNotEmpty(map).isValid();
        assert !VerificationTool.mapIsNotEmpty(null).isValid();
        assert !VerificationTool.mapIsNotEmpty(Map.of()).isValid();
    }

    @Test
    void isNotMatch() {
        assert !VerificationTool.isNotMatch(1, (x) -> x > 0).isValid();
        assert !VerificationTool.isNotMatch(null, (x) -> x.equals(1)).isValid();
        assert VerificationTool.isNotMatch(1, x -> x < 0).isValid();
    }

    @Test
    void isMatch() {
        assert VerificationTool.isMatch(1, x -> x == 1).isValid();
        assert !VerificationTool.isMatch(1, x -> x > 1).isValid();
        assert !VerificationTool.isMatch(null, x -> x.equals(1)).isValid();
    }
}