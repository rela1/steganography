package com.bmpsteg.benchmark;

import com.bmpsteg.steg.InsufficientSpaceException;
import com.bmpsteg.steg.RGBBitsSteganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by orsic on 16.01.16..
 */
public class SteganographyBenchmark {

    private ArrayList<ComponentPair> componentParams;

    private byte[] generateData(int n_bytes){
        byte[] random_bytes = new byte[n_bytes];
        new Random().nextBytes(random_bytes);
        return random_bytes;
    }

    public SteganographyBenchmark(){
        componentParams =  new ArrayList<>();
        for(int i=1; i<=3; i++){
            for(int j=1; j<=8; j++){
                componentParams.add(new ComponentPair(i,j));
            }
        }
    }

    public static BufferedImage openString(String filename){
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.err.print("Invalid file name");
            return null;
        }
        return originalImage;
    }
    public static BufferedImage openURL(String urlStr){
        BufferedImage originalImage = null;
        try {
            URL url = new URL(urlStr);
            originalImage = ImageIO.read(url);
        } catch (IOException e) {
            System.err.print("Invalid URL");
            return null;
        }
        return originalImage;
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void evaluate(String folder, BufferedImage originalImage){
        for(ComponentPair componentParam: componentParams) {
            System.out.println(componentParam);
            RGBBitsSteganography algorithm = new RGBBitsSteganography(componentParam.componentsToUse, componentParam.bitsPerComponent);
            int max_bytes = algorithm.getMaxBytes(originalImage);
            byte[] toHide = this.generateData(max_bytes);
            BufferedImage copied = deepCopy(originalImage);
            try {
                algorithm.hideData(toHide, copied);
                File outputfile = new File(folder + componentParam + ".png");
                ImageIO.write(copied, "png", outputfile);
            } catch (InsufficientSpaceException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        if(args.length != 2){
            System.err.print("Expected 2 command line params: target folder and original image path!");
            return;
        }
        SteganographyBenchmark benchmark = new SteganographyBenchmark();
        String target_folder = args[0];
        String original_image = args[1];
        benchmark.evaluate(target_folder, openString(original_image));
    }

}
