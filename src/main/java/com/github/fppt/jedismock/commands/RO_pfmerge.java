package com.github.fppt.jedismock.commands;

import com.github.fppt.jedismock.RedisBase;
import com.github.fppt.jedismock.Response;
import com.github.fppt.jedismock.Slice;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

import static com.github.fppt.jedismock.Utils.deserializeObject;
import static com.github.fppt.jedismock.Utils.serializeObject;

class RO_pfmerge extends AbstractRedisOperation {
    RO_pfmerge(RedisBase base, List<Slice> params) {
        super(base, params,null, 0, null);
    }

    Slice response() {
        Slice key = params().get(0);
        Slice data = base().getValue(key);
        boolean first;

        Set<Slice> set;
        if (data == null) {
            set = Sets.newHashSet();
            first = true;
        } else {
            set = deserializeObject(data);
            first = false;
        }
        for (Slice v : params().subList(1, params().size())) {
            Slice src = base().getValue(v);
            if (src != null) {
                Set<Slice> s = deserializeObject(src);
                set.addAll(s);
            }
        }

        Slice out = serializeObject(set);
        if (first) {
            base().putValue(key, out);
        } else {
            base().putValue(key, out, null);
        }
        return Response.OK;
    }
}
