
local current = redis.call("get",KEYS[1])
if current == ARGV[1]
then
    local count = redis.call("decr",KEYS[2])
    if count == 0
        then
        redis.call("del",KEYS[1])
        redis.call("del",KEYS[2])
        return 1
    end
end
return 0