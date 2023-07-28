import { useEffect, useState } from "react";

function useLocalState(defaultValue, key) {
  const [value, setValue] = useState(() => {
    const localStoragedValue = localStorage.getItem(key);

    return localStoragedValue !== null ? JSON.parse(localStoragedValue) : defaultValue;
  });

  useEffect(() => {
    localStorage.setItem(key, JSON.stringify(value));
  }, [key, value]);

  return [value, setValue];
}

export { useLocalState };
