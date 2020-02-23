package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import scc210game.engine.ecs.Component;


public class Steps extends Component {

	public int count = 5;
	public int oldCount = 0;

	static {
		register(Steps.class, s -> {
			final JsonObject json = Jsoner.deserialize(s, new JsonObject());
			int cnt = (int) json.get("count");
			int ocnt = (int) json.get("oldCount");

			return new Steps(cnt, ocnt);
		});
	}

	public Steps(int cnt, int ocnt) {
		this.count = cnt;
		this.oldCount = ocnt;
	}

	@Override
	public String serialize() {
		final Jsonable json = new JsonObject() {{
			this.put("count", Steps.this.count);
			this.put("oldCount", Steps.this.oldCount);
		}};

		return json.toJson();
	}


}
