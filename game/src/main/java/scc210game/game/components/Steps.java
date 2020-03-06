package scc210game.game.components;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;
import scc210game.engine.ecs.Component;
import scc210game.game.utils.LoadJsonNum;


public class Steps extends Component {

	public int count = 5;
	public int oldCount = 0;

	static {
		register(Steps.class, j -> {
            var json = (JsonObject) j;
			int cnt = LoadJsonNum.loadInt(json.get("count"));
			int ocnt = LoadJsonNum.loadInt(json.get("oldCount"));

			return new Steps(cnt, ocnt);
		});
	}

	public Steps(int cnt, int ocnt) {
        this.count = cnt;
        this.oldCount = ocnt;
    }

    @Override
    public Jsonable serialize() {
        final Jsonable json = new JsonObject() {{
            this.put("count", Steps.this.count);
            this.put("oldCount", Steps.this.oldCount);
        }};

        return json;
    }





}
