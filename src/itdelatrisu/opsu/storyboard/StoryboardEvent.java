package itdelatrisu.opsu.storyboard;

import itdelatrisu.opsu.ErrorHandler;

public abstract class StoryboardEvent implements Comparable<StoryboardEvent> {

	 // http://google.com
	
	/** Undefined type (alias) */
	public static final int UNDEFINED = -1;

	/** Event types */ // print as string in osb, as int in osu
	public static final String TYPE_BACKGROUND = "Background", TYPE_VIDEO = "Video", TYPE_BREAK = "Break",
			TYPE_BACKGROUND_COLOR = "BackgroundColor", TYPE_SPRITE = "Sprite", TYPE_SAMPLE = "Sample",
			TYPE_ANIMATION = "Animation";

	/** Event layers */
	public static final String LAYER_BACKGROUND = TYPE_BACKGROUND, LAYER_FAIL = "Fail", LAYER_PASS = "Pass",
			LAYER_FOREGROUND = "Foreground";

	/** Origin */
	public static final String ORIGIN_TOP_LEFT = "TopLeft", ORIGIN_TOP_CENTER = "TopCentre",
			ORIGIN_TOP_RIGHT = "TopRight", ORIGIN_CENTER_LEFT = "CentreLeft", ORIGIN_CENTER = "Centre",
			ORIGIN_CENTER_RIGHT = "CentreRight", ORIGIN_BOTTOM_LEFT = "BottomLeft",
			ORIGIN_BOTTOM_CENTER = "BottomCentre", ORIGIN_BOTTOM_RIGHT = "BottomRight";

	/** Easing */
	public static final int EASING_NONE = 0, EASING_IN = 1, EASING_OUT = 2;

	/** Loop types */
	public static final String LOOP_FOREVER = "LoopForever", LOOP_ONCE = "LoopOnce";

	/** Transform types */
	public static final String FADE = "F", MOVE = "M", MOVE_X = "MX", MOVE_Y = "MY", SCALE = "S", VECTOR_SCALE = "V",
			ROTATE = "R", COLOR = "C", PARAMETER = "P",
			// specials
			LOOP = "L", TRIGGER = "T";

	/** Parameter types */
	public static final String ADD_COLOR_BLENDING = "A", HORIZONTAL_FLIP = "H", VERTICAL_FLIP = "V";

	/** Trigger types */
	public static final String TRIGGER_HITSOUND = "HitSound", TRIGGER_PASS = "Passing", TRIGGER_FAIL = "Failing";

	/**
	 * Event layer, can be any of the following:
	 * <ul>
	 * <li>{@link #LAYER_BACKGROUND}</li>
	 * <li>{@link #LAYER_FOREGROUND}</li>
	 * <li>{@link #PASS}</li>
	 * <li>{@link #FAIL}</li>
	 * </ul>
	 */
	public final String layer;

	/**
	 * Event type, can be any of the following:
	 * <ul>
	 * <li>{@link #TYPE_BACKGROUND}</li>
	 * <li>{@link #TYPE_VIDEO}</li>
	 * <li>{@link #TYPE_BREAK}</li>
	 * <li>{@link #TYPE_BACKGROUND_COLOR}</li>
	 * <li>{@link #TYPE_SPRITE}</li>
	 * <li>{@link #TYPE_SAMPLE}</li>
	 * <li>{@link #TYPE_ANIMATION}</li>
	 * </ul>
	 */
	public final String type;

	/**
	 * Start time of the event,
	 * determines event duration.
	 */
	public int startTime;

	/**
	 * End time of the event,
	 * determines event duration.
	 */
	public int endTime;

	protected StoryboardEvent(String layer, String type, int start, int end) {
		this.layer = layer;
		this.type = type;
		this.startTime = start;
		this.endTime = end;
	}

	public static StoryboardEvent parse(String s) {
		String[] parts = s.split(",");
		String etype = parts[0];
		String path = "";
		try {
			if (parts.length > 3)
				path = parts[3].replaceAll("\"", "");

			if (etype.equals(TYPE_SPRITE)) {
				return new SpriteEvent(path, parts[1], parts[2], Float.parseFloat(parts[4]),
						Float.parseFloat(parts[5]));
			} else if (etype.equals(TYPE_ANIMATION)) {
				// TODO
			} else if (etype.equals(TYPE_SAMPLE)) {
				return new SampleEvent(path, Integer.parseInt(parts[1]), Integer.parseInt(parts[4]), 
						parts[2]);
			} else if (etype.equals(TYPE_BACKGROUND)) {
				// TODO
			} else if (etype.equals(TYPE_VIDEO)) {
				// TODO
			} else if (etype.equals(TYPE_BREAK)) {
				// TODO
			} else if (etype.equals(TYPE_BACKGROUND_COLOR)) {
				// TODO
			}
		} catch (Exception e) {
			ErrorHandler.error("Failed to parse event", e, false);
		}

		return new UndefinedEvent(parts);

	}
	
	/**
	 * Retrieve the event's duration.
	 * This is re-computed every call to ensure that changes to the start and end times are
	 * reflected here.
	 */
	public int getEventDuration(){
		return this.endTime - this.startTime;
	}

	@Override
	public int compareTo(StoryboardEvent that) {
		return (this.startTime - that.startTime) + (this.endTime - that.endTime);
	}

	/**
	 * Represents an undefined event in a storyboard
	 */
	public static class UndefinedEvent extends StoryboardEvent {

		public final String[] lineparts;

		public UndefinedEvent(String[] lineparts) {
			super(null, null, UNDEFINED, UNDEFINED);
			this.lineparts = lineparts;
		}

		public String toString() {
			return String.join(",", lineparts);
		}
	}
}
