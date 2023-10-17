package dev.djxjd.fallgods.beans.wrappers;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MinigameData {
	
	private int plays;
	private float occurrence;
	private int qualifications;
	private float qualificationRate;
	private int eliminations;
	private float eliminationRate;
	private int finishes;
	private float finishRate;
	private int fumbles;
	private float fumbleRate;
	private int mvps;
	private float mvpRate;

}
