package CompTwo;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.*;
import java.io.*;

public class Comp {
	private static double[][] eval(File inp, File out, int x) throws IncompatException, IOException {
		if (inp.list().length < 3)
			if (inp.list().length == 2)
				return eval(inp, out);
			else
				throw new IncompatException("Contains less than two folders: "+inp.list().length);
		if (x<0 || x>inp.list().length-1)
			throw new IncompatException("Bounding error: _key located at nonexistent location");

		int l = getAddress(inp, 0).list().length, t = 0;
		for (int y=1; y<inp.list().length; y++) 
			l = l<getAddress(inp, y).list().length? getAddress(inp, y).list().length: l;
		double[][] trial = new double[(inp.list().length-1)*2][l];
		for (int y=0; y<inp.list().length; y++)
			if (y!=x) {
				double[][] temp = comp(new File[]{getAddress(inp, x), getAddress(inp, y)}, out);
				trial[2*t] = temp[0];
				trial[2*t+++1] = temp[1];
			}
		return trial;
	}
	public static double[][] eval(File inp, File out) throws IncompatException, IOException {
		int x;
		try {
			String[] temp = inp.list();
			if (temp.length < 2)
				throw new IncompatException("Contains less than two folders: "+temp.length);
			for (x=0; x<=temp.length; x++)
				if (temp[x].substring(temp[x].length()>3? temp[x].length()-4: 0).equalsIgnoreCase("_key"))
					break;
		}
		catch(IncompatException e) {throw e;}
		catch(Exception e) {x=-1;}
		if (inp.list().length > 2)
			if (x == -1)
				throw new IncompatException("Folder does not contain a key(\"_key\")");
			else
				return eval(inp, out, x);
		
		return comp(new File[]{getAddress(inp, 0), getAddress(inp, 1)}, out);
	}
	public static double[][] comp(File[] inP, File out) throws IOException {
		int l = inP[0].list().length<inP[1].list().length? inP[0].list().length: inP[1].list().length;
		double[][] per = new double[2][l];

		for(int z=0; z<l; z++) {
			BufferedImage[] comp = {ImageIO.read(getAddress(inP[0], z)), ImageIO.read(getAddress(inP[1], z))};
			BufferedImage rastM = new BufferedImage(comp[0].getWidth(), comp[0].getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			BufferedImage compM = new BufferedImage(comp[0].getWidth(), comp[0].getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			for (int x=0; x<comp[0].getWidth(); x++)
				for (int y=0; y<comp[0].getHeight(); y++) {
					Color[] c = new Color[2];
					for (int a=0; a<c.length; a++)
						c[a] = new Color(comp[a].getRGB(x, y));
					per[0][z] += (c[0].getRed() != c[1].getRed() || c[0].getGreen() != c[1].getGreen() || c[0].getBlue() != c[1].getBlue())? 0: 1;
					per[1][z] += 1-Math.abs((double)((c[0].getRed()-c[1].getRed())+(c[0].getGreen()-c[1].getGreen())+(c[0].getBlue()-c[1].getBlue()))/(255*3));
					rastM.setRGB(x, y, (c[0].getRed() != c[1].getRed() || c[0].getGreen() != c[1].getGreen() || c[0].getBlue() != c[1].getBlue())? Color.WHITE.hashCode(): Color.BLACK.hashCode());
					compM.setRGB(x, y, new Color(Math.abs(c[0].getRed()-c[1].getRed()), Math.abs(c[0].getGreen()-c[1].getGreen()), Math.abs(c[0].getBlue()-c[1].getBlue())).hashCode());
				}
			per[0][z] /= (double)comp[0].getHeight()*comp[0].getWidth();
			per[1][z] /= (double)comp[0].getHeight()*comp[0].getWidth();
			
			File temp1 = new File(String.format("%s\\%s && %s\\%06d_r.png", out.getCanonicalPath(), inP[0].getName(), inP[1].getName(), z));
			File temp2 = new File(String.format("%s\\%s && %s\\%06d_c.png", out.getCanonicalPath(), inP[0].getName(), inP[1].getName(), z));
			temp1.mkdirs();
			temp2.mkdirs();
			ImageIO.write(rastM, "png", temp1);
			ImageIO.write(compM, "png", temp2);
			
			System.out.print(z + (z!=l-1? " ": "\n"));
		}
		
		PrintWriter txtOut = new PrintWriter(String.format("%s\\%s && %s\\comp.txt", out.getCanonicalPath(), inP[0].getName(), inP[1].getName()));
		for (int x=0; x<per[0].length; x++)
			for (int y=0; y<per.length; y++)
				txtOut.printf("%.5f%s", per[y][x], y!=per.length-1? ", ": "\n");
		txtOut.close();
		
		return per;
	}
	
	private static double[][] evalNoImg(File inp, File out, int x) throws IncompatException, IOException {
		if (inp.list().length < 3)
			if (inp.list().length == 2)
				return eval(inp, out);
			else
				throw new IncompatException("Contains less than two folders: "+inp.list().length);
		if (x<0 || x>inp.list().length-1)
			throw new IncompatException("Bounding error: _key located at nonexistent location");

		int l = getAddress(inp, 0).list().length, t = 0;
		for (int y=1; y<inp.list().length; y++) 
			l = l<getAddress(inp, y).list().length? getAddress(inp, y).list().length: l;
		double[][] trial = new double[(inp.list().length-1)*2][l];
		for (int y=0; y<inp.list().length; y++)
			if (y!=x) {
				double[][] temp = compNoImg(new File[]{getAddress(inp, x), getAddress(inp, y)}, out);
				trial[2*t] = temp[0];
				trial[2*t+++1] = temp[1];
			}
		return trial;
	}
	public static double[][] evalNoImg(File inp, File out) throws IncompatException, IOException {
		int x;
		try {
			String[] temp = inp.list();
			if (temp.length < 2)
				throw new IncompatException("Contains less than two folders: "+temp.length);
			for (x=0; x<=temp.length; x++)
				if (temp[x].substring(temp[x].length()>3? temp[x].length()-4: 0).equalsIgnoreCase("_key"))
					break;
		}
		catch(IncompatException e) {throw e;}
		catch(Exception e) {x=-1;}
		if (inp.list().length > 2)
			if (x == -1)
				throw new IncompatException("Folder does not contain a key(\"_key\")");
			else
				return evalNoImg(inp, out, x);
		
		return compNoImg(new File[]{getAddress(inp, 0), getAddress(inp, 1)}, out);
	}
	public static double[][] compNoImg(File[] inP, File out) throws IOException {
		int l = inP[0].list().length<inP[1].list().length? inP[0].list().length: inP[1].list().length;
		double[][] per = new double[2][l];

		for(int z=0; z<l; z++) {
			BufferedImage[] comp = {ImageIO.read(getAddress(inP[0], z)), ImageIO.read(getAddress(inP[1], z))};
			for (int x=0; x<comp[0].getWidth(); x++)
				for (int y=0; y<comp[0].getHeight(); y++) {
					Color[] c = {new Color(comp[0].getRGB(x, y)), new Color(comp[1].getRGB(x, y))};
					per[0][z] += (c[0].getRed() != c[1].getRed() || c[0].getGreen() != c[1].getGreen() || c[0].getBlue() != c[1].getBlue())? 0: 1;
					per[1][z] += 1-Math.abs((double)((c[0].getRed()-c[1].getRed())+(c[0].getGreen()-c[1].getGreen())+(c[0].getBlue()-c[1].getBlue()))/(255*3));
				}
			per[0][z] /= (double)comp[0].getHeight()*comp[0].getWidth();
			per[1][z] /= (double)comp[0].getHeight()*comp[0].getWidth();
			
			System.out.print(z + (z!=l-1? " ": "\n"));
		}
		(new File(String.format("%s\\%s && %s", out.getCanonicalPath(), inP[0].getName(), inP[1].getName()))).mkdirs();
		PrintWriter txtOut = new PrintWriter(String.format("%s\\%s && %s\\comp.txt", out.getCanonicalPath(), inP[0].getName(), inP[1].getName()));
		for (int x=0; x<per[0].length; x++)
			for (int y=0; y<per.length; y++)
				txtOut.printf("%.5f%s", per[y][x], y!=per.length-1? ", ": "\n");
		txtOut.close();
		
		return per;
	}
	
	public static File getAddress(File inp, int x, int y) throws IOException {return getAddress(new File(inp.getCanonicalPath().concat("\\"+inp.list()[x])), y);}
	public static File getAddress(File inp, int x) throws IOException {return new File(inp.getCanonicalPath().concat("\\"+inp.list()[x]));}
	
	public static void main(String args[]) throws IOException, IncompatException {
		double[][] gennedPer = evalNoImg(new File("trialInp"), new File("trialOut"));
		for (int x=0; x<gennedPer[0].length; x++)
			for (int y=0; y<gennedPer.length; y++)
				System.out.printf("%.5f, "+(y!=gennedPer.length-1? "": (gennedPer[y-1][x]<gennedPer[y][x])+"\n"), gennedPer[y][x]);
	}
}