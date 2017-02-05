package cvora.googledirectionsapitest.routing;

import java.util.List;

public interface Parser {
    List<Route> parse() throws RouteException;
}