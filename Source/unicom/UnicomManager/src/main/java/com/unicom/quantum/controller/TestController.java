package com.unicom.quantum.controller;

import com.unicom.quantum.component.util.DataTools;
import com.unicom.quantum.component.util.HexUtils;
import com.unicom.quantum.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jerry
 * @version 1.0
 * @note
 */
@RequestMapping("/test")
@RestController
public class TestController {


    @Autowired
    private TestMapper testMapper;

    @RequestMapping("/a")
    public int modify() throws Exception {
        byte[] bytes = HexUtils.hexStringToBytes("E193CB8148C56C76577F33CE838C401691097C7476D79161B1EB7AE06BB3E82C");
        byte[] encKey =DataTools.encryptMessage(bytes);
        testMapper.updateEnc(encKey,"1112022012300001");

        byte[] bytes2 = HexUtils.hexStringToBytes("BD44DC6758749A146F81504F0E2F05547A4782A651B0821165FBC8481DF125C6");
        byte[] encKey2 =DataTools.encryptMessage(bytes2);
        testMapper.updateEnc(encKey2,"1112022012300002");

        byte[] bytes3 = HexUtils.hexStringToBytes("A14C40AA8D5F6A06C4161460C5B1FCE674A94C2A69686D525E050F3ADB09D055");
        byte[] encKey3 =DataTools.encryptMessage(bytes3);
        testMapper.updateEnc(encKey3,"1112022012300003");

        byte[] bytes4 = HexUtils.hexStringToBytes("D896A61C837F3A82C358A078014D902DFB306E84FCD5D747F794CFBADB21343E");
        byte[] encKey4 =DataTools.encryptMessage(bytes4);
        testMapper.updateEnc(encKey4,"1112022012300004");

        byte[] bytes5 = HexUtils.hexStringToBytes("661B1151C785DC2735E5337B4F6502D7817F936C17F8E88626DFFA6226FB0E3B");
        byte[] encKey5 =DataTools.encryptMessage(bytes5);
        testMapper.updateEnc(encKey5,"1112022012300005");

        byte[] bytes6 = HexUtils.hexStringToBytes("BC56EE76D8BA06056596F24393C318A43A9458EDD2D3A11B1CCEF57FD2628C55");
        byte[] encKey6 =DataTools.encryptMessage(bytes6);
        testMapper.updateEnc(encKey6,"1112022012300006");

        byte[] bytes7 = HexUtils.hexStringToBytes("0B9A63165E70A0D4290FAAF6EBD4355CEC5F6D29040D88DDC0BF230C3B7B5371");
        byte[] encKey7 =DataTools.encryptMessage(bytes7);
        testMapper.updateEnc(encKey7,"1112022012300007");

        byte[] bytes8 = HexUtils.hexStringToBytes("01FC59759FA3FB9E61B51FA06AC48053F6F026173FC3A4515CE446F8F82CE342");
        byte[] encKey8 =DataTools.encryptMessage(bytes8);
        testMapper.updateEnc(encKey8,"1112022012300008");

        byte[] bytes9 = HexUtils.hexStringToBytes("78EF65B4F293241117AA267B4A7440619346557DE68786BC51A25B9028E2894D");
        byte[] encKey9 =DataTools.encryptMessage(bytes9);
        testMapper.updateEnc(encKey9,"1112022012300009");

        byte[] bytes10 = HexUtils.hexStringToBytes("B95724CADFDBE24960AE0F936CCC5231DEB015F5D54ADBA6DD18DB0852B0E4C5");
        byte[] encKey10 =DataTools.encryptMessage(bytes10);
        testMapper.updateEnc(encKey10,"1112022012300010");
        return 1;
    }

}
