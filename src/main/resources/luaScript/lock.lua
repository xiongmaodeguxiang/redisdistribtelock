
local res = redis.call("SETNX",KEYS[1],ARGV[1])
if res == 1 then
    local flag = redis.call("EXPIRE",KEYS[1],ARGV[2])
    redis.call("incr",KEYS[2])
    return flag
end
return 0