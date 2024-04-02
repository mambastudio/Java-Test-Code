/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.memory.map;

import java.lang.foreign.*;
import java.lang.invoke.*;
import java.util.Random;


/**
 *
 * @author user
 */
public class JavaMemoryMap {

    /**
     * @param argv
     */
    public final static void main(String[] argv) {
		if (argv.length != 1) {
			System.out.println("Usage: perf <n>");
			return;
		}
		int n = Integer.parseInt(argv[0]);
		System.out.println("N=" + n);
		try (Arena arena = Arena.ofConfined()) {
			SequenceLayout layout = MemoryLayout.sequenceLayout(n,
				MemoryLayout.structLayout(
					ValueLayout.JAVA_FLOAT.withName("x"),
					ValueLayout.JAVA_FLOAT.withName("y"),
					ValueLayout.JAVA_INT.withName("flags")
				)
			).withName("mystruct");
			VarHandle xHandle = layout.varHandle(MemoryLayout.PathElement.sequenceElement(), MemoryLayout.PathElement.groupElement("x"));
			VarHandle yHandle = layout.varHandle(MemoryLayout.PathElement.sequenceElement(), MemoryLayout.PathElement.groupElement("y"));
			VarHandle flagsHandle = layout.varHandle(MemoryLayout.PathElement.sequenceElement(), MemoryLayout.PathElement.groupElement("flags"));
			MemorySegment mem = arena.allocate(layout);
			Random rng = new Random();
			for (long i = 0; i < n; i++) {
				xHandle.set(mem, i, rng.nextFloat());
				yHandle.set(mem, i, rng.nextFloat());
				flagsHandle.set(mem, i, rng.nextInt());                                
			}

			long t0 = System.nanoTime();
			float sum = 0.0f;
			for (int i = 0; i < n; i++) {
				float x = (float) xHandle.get(mem, i);
				float y = (float) yHandle.get(mem, i);
				int flags = (int) flagsHandle.get(mem, i);
				if ((flags & 256) == 0) continue;
				float fx = x;
				float fy = y;
				sum += fx*fy;
			}
			long dt = System.nanoTime() - t0;
			System.out.println("sum="+sum+" "+(float)n/(dt*1e-9)+" it/s");
		}
	}
    
}
